package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;

public interface SurveyService {

    /** 全てのアンケートを取得します */
    List<Survey> findAllSurveys();

    /**
     * アンケート詳細を取得します。
     * 
     * @param surveyId アンケートID
     * @return アンケートドメインモデル
     * @throws IllegalArgumentException アンケートが存在しない場合
     */
    Survey findSurveyById(Integer surveyId) throws IllegalArgumentException;

    /**
     * 既存アンケートを流用して、新規登録用の雛形を取得する
     */
    Survey findSurveyAsDraftCopy(Integer surveyId);

    /**
     * 対象ユーザーがアンケート対象かを確認します
     */
    boolean canResponseBySurveyid(Integer surveyId, String username);

    Survey saveSurvey(Survey survey);

    boolean tryChangeStatusById(Integer id, SurveyStatus status);

    List<Survey> findSurveysByUsername(String username);

    List<Survey> findAvailableSurveysByUsername(String username);
}