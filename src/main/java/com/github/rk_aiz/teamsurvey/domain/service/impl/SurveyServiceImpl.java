package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.service.QuestionService;
import com.github.rk_aiz.teamsurvey.domain.service.ResponseService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;
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
     * @return アンケートドメインモデル
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
        Survey original = surveyRepository.findById(id);
        return original.toDraftCopy();
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
    public boolean tryChangeStatusById(Integer id, SurveyStatus status) throws IllegalArgumentException {

        Survey survey = this.findSurveyById(id);

        // 公開(PUBLISHED)への変更時のみ、整合性チェックを行う
        if (status == SurveyStatus.PUBLISHED && !survey.canPublish()) {
            throw new IllegalArgumentException("設問に不備があるためステータスを公開に変更できません。");
        }

        survey.setStatus(status);
        surveyRepository.set(survey);

        return true;
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
    public List<Survey> findAvailableSurveys() {
        return this.surveyRepository.findAll()
                .stream()
                .filter(survey -> survey.getStatus() != SurveyStatus.DRAFT)
                .toList();
    }
}