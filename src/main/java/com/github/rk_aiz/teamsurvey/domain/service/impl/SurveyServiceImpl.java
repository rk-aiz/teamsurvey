package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.exception.ServiceRuleException;
import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.model.question.SingleChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.service.AnswerOptionService;
import com.github.rk_aiz.teamsurvey.domain.service.QuestionService;
import com.github.rk_aiz.teamsurvey.domain.service.ResponseService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;
import com.github.rk_aiz.teamsurvey.domain.service.UserGroupService;
import com.github.rk_aiz.teamsurvey.domain.type.ResultVisibility;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyTargetGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final ResponseService responseService;
    private final QuestionService questionService;
    private final AnswerOptionService answerOptionService;
    private final SurveyTargetGroupRepository surveyTargetGroupRepository;
    private final UserGroupService userGroupService;

    /**
     * アンケート一覧を取得します。
     */
    @Override
    public List<Survey> findAllSurveys() {
        return surveyRepository.findAll();
    }

    /**
     * アンケート詳細を取得します。
     * 
     * @param surveyId アンケートID
     * @throws IllegalArgumentException アンケートが存在しない場合
     */
    @Override
    public Survey findSurveyById(Integer surveyId) throws IllegalArgumentException {
        Survey survey = surveyRepository.findById(surveyId);

        if (survey == null) {
            throw new IllegalArgumentException("指定されたアンケートが見つかりません: " + surveyId);
        }

        return survey;
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
        if (survey.getSurveyId() == null) {
            // 新規登録
            surveyRepository.add(survey);
            return survey;
        }

        // 更新処理
        // DB上の現在の状態を取得
        Survey currentDbSurvey = this.findSurveyById(survey.getSurveyId());

        // DB上のステータスがDRAFT以外の場合、設問構成変更を許可しない
        if (currentDbSurvey.getStatus() != SurveyStatus.DRAFT) {
            // 変更を無視し、DBの値を強制的にセット
            survey.setQuestions(currentDbSurvey.getQuestions());
        } else {
            // DRAFTの場合のみ、設問の構成変更を行う
            Set<Integer> questionIds = survey.getQuestions() == null ? Set.of()
                    : survey.getQuestions().stream()
                            .map(q -> q.getQuestionId())
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());

            // DBに存在するがフォームに含まれていない質問を削除
            if (currentDbSurvey.getQuestions() != null) {
                currentDbSurvey.getQuestions().stream()
                        .map(q -> q.getQuestionId())
                        .filter(id -> !questionIds.contains(id))
                        .forEach(questionService::removeQuestion);
            }
        }

        surveyRepository.set(survey);
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
                survey.getQuestions().stream().forEach(q -> {
                    if (q instanceof SingleChoiceQuestion scq) {
                        AnswerOption answerOption = scq.getAnswerOption();
                        Integer newId = answerOptionService.createSnapshot(answerOption.getAnswerOptionId());
                        answerOption.setAnswerOptionId(newId);
                    }
                });
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
                // 修正: 「現在のステータス」が終了済みかどうかをチェックする
                if (!survey.getStatus().isAtLeast(SurveyStatus.CLOSED)) {
                    throw new ServiceRuleException("アンケート削除前に、終了に変更してください");
                }
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + newStatus);
        }

        survey.setStatus(newStatus);
        return surveyRepository.set(survey);
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
                        && !answeredSurveyIds.contains(survey.getSurveyId()))
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

    @Override
    public void updateTargetGroups(Integer surveyId, List<Integer> groupIds) {
        
        // 1. 既存の紐付けを全削除
        surveyTargetGroupRepository.removeBySurveyId(surveyId);

        if (groupIds == null || groupIds.isEmpty()) {
            return;
        }

        // 2. バルクインサートは安全のため分割して登録 (Defensive Programming)
        // DBのパラメータ数上限やパケットサイズ制限を回避するため、一定件数ごとに分割してINSERTする
        // ※これは参考用(まずバッチサイズを超えることはない) 
        final int BATCH_SIZE = 1000;

        for (int i = 0; i < groupIds.size(); i += BATCH_SIZE) {
            int end = Math.min(groupIds.size(), i + BATCH_SIZE);
            List<Integer> batchList = groupIds.subList(i, end);
            surveyTargetGroupRepository.add(surveyId, batchList);
        }

        return;
    }
}