package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;

public interface ResponseService {

    /** 全ての回答を取得します */
    List<Response> findAllResponses();

    List<Response> findResponseByUsername(String username);

    List<Response> findResponseBySurveyId(Integer surveyId);

    /**
     * 回答詳細を取得します。
     */
    Response findResponseById(Integer responseId);

    boolean saveResponse(Survey survey, Response response);

    Response createNewResponseBySurvey(Survey survey, UserAccount account);
}