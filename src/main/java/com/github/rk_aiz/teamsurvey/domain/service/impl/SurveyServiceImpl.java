package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.exception.ServiceRuleException;
import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.model.question.SingleChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.service.AnswerOptionService;
import com.github.rk_aiz.teamsurvey.domain.service.QuestionService;
import com.github.rk_aiz.teamsurvey.domain.service.ResponseService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;
import com.github.rk_aiz.teamsurvey.domain.type.ResultVisibility;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final ResponseService responseService;
    private final QuestionService questionService;
    private final AnswerOptionService answerOptionService;

    /**
     * アンケート一覧を取得します。
     */
    @Override
    public List<Survey> findAllSurveys() {
        return surveyRepository.findAll()
                .stream()
                .filter(survey -> { 
                    return survey.getStatus() != SurveyStatus.DELETED; })
                .toList();
    }

    /**
     * アンケート詳細を取得します。
     * 
     * @param surveyId アンケートID
     * @throws IllegalArgumentException アンケートが存在しない場合
     */
    @Override
    public Survey findSurveyById(Integer surveyId) throws IllegalArgumentException {
        return Optional.ofNullable(surveyId).map(surveyRepository::findById)
                .orElseThrow(() -> new IllegalArgumentException("指定されたアンケートが見つかりません: " + surveyId));
    }

    /**
     * ユースケース: 既存アンケートを流用して、新規登録用の雛形を取得する
     */
    @Override
    public Survey findSurveyAsDraftCopy(Integer id) {
        return surveyRepository.findById(id).toDraftCopy();
    }

    @Override
    public Survey saveSurvey(Survey survey) {

        if (survey.getId() == null) {
            // 新規登録
            surveyRepository.add(survey);
            return survey;
        }

        // 更新処理
        // DB上の現在の状態を取得
        Survey currentDbSurvey = this.findSurveyById(survey.getId());

        if (currentDbSurvey == null)
            throw new IllegalArgumentException(
                    "更新対象のアンケートが見つかりません");

        survey.setStatus(currentDbSurvey.getStatus());
        surveyRepository.updateHeader(survey);

        // DB上のステータスがDRAFTの場合のみ、設問構成を更新
        if (currentDbSurvey.getStatus() == SurveyStatus.DRAFT) {

            // 1. 削除対象の特定と削除
            // 現在DBに登録されている設問IDリスト
            List<Integer> currentQuestionIds = currentDbSurvey.getQuestions().stream()
                    .map(Question::getId)
                    .toList();

            // 今回のリクエストに含まれる設問IDリスト(新規追加分はIDがnullなので除外)
            List<Integer> keepingQuestionIds = survey.getQuestions().stream()
                    .filter(Objects::nonNull)
                    .map(Question::getId)
                    .filter(Objects::nonNull)
                    .toList();

            // DBにはあるがリクエストにはないIDを削除
            currentQuestionIds.stream()
                    .filter(id -> !keepingQuestionIds.contains(id))
                    .forEach(questionService::removeQuestion);

            // 2. 追加・更新
            survey.getQuestions().stream()
                    .filter(Objects::nonNull)
                    .map(question -> {
                        question.setSurveyId(survey.getId());
                        return question;
                    }).forEach(questionService::saveQuestion);
        }

        return survey;
    }

    @Override
    public boolean tryChangeStatusById(Integer id, SurveyStatus newStatus) {

        Survey survey = this.findSurveyById(id);

        // ステータス変更整合性チェック
        switch (newStatus) {
            case DRAFT -> {
                // 修正: 「新しいステータス」ではなく「現在のステータス」をチェックする
                if (survey.getStatus().isAtLeast(SurveyStatus.PUBLISHED)) {
                    throw new ServiceRuleException("公開済みのアンケートを下書きに変更できません。");
                }
            }
            case PUBLISHED -> {
                if (!survey.canPublish()) {
                    throw new ServiceRuleException("設問に不備があるためステータスを公開に変更できません。");
                }

                // 回答パターンをスナップショットに変更
                this.createSnapshotForQuestions(survey);
            }
            case SUSPENDED -> {
                if (survey.getStatus() != SurveyStatus.PUBLISHED) {
                    throw new ServiceRuleException("アンケートを一時停止状態に変更できません。");
                }
            }
            case CLOSED -> {
                if (!survey.getStatus().canChangeToClose()) {
                    throw new ServiceRuleException("アンケートを終了状態に変更できません。");
                }
            }
            case DELETED -> {
                // 公開中以外(下書き or 終了)であれば削除可能とする
                if (survey.getStatus() == SurveyStatus.PUBLISHED) {
                    throw new ServiceRuleException("公開中のアンケートは削除できません。先にステータスを変更してください");
                }
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + newStatus);
        }

        survey.setStatus(newStatus);
        return surveyRepository.updateHeader(survey);
    }

    private void createSnapshotForQuestions(Survey survey) {
        survey.getQuestions().stream().forEach(q -> {
            if (q instanceof SingleChoiceQuestion scq) {
                AnswerOption answerOption = scq.getAnswerOption();
                Integer newId = answerOptionService.createSnapshot(answerOption.getAnswerOptionId());
                answerOption.setAnswerOptionId(newId);
            }
        });
    }

    @Override
    public List<Survey> findSurveysByUsername(String username) {
        return this.surveyRepository.findByUsername(username);
    }

    @Override
    public List<Survey> findAvailableSurveysByUsername(String username) {
        Set<Integer> answeredSurveyIds = this.responseService.findResponseByUsername(username)
                .stream()
                .map(Response::getSurveyId)
                .collect(Collectors.toSet());

        return this.findSurveysByUsername(username)
                .stream()
                .filter(survey -> survey.getStatus() == SurveyStatus.PUBLISHED
                        && !answeredSurveyIds.contains(survey.getId()))
                .toList();
    }

    @Override
    public boolean canResponseBySurveyid(Integer surveyId, String username) {
        return this.surveyRepository.canResponse(surveyId, username);
    }

    @Override
    public Survey getEmptySurvey() {
        return Survey.builder()
                .status(SurveyStatus.DRAFT)
                .resultVisibility(ResultVisibility.ADMIN_ONLY)
                .build();
    }
}