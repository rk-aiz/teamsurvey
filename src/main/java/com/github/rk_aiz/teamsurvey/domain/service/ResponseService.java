package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;

public interface ResponseService {

    /** 全ての回答を取得します */
    List<Response> findAllResponses();

    /**
     * 回答詳細を取得します。
     */
    Response findResponseById(Integer responseId) throws IllegalArgumentException;

    boolean saveResponse(Response survey);

    Response createNewResponseBySurvey(Survey survey, LoginUser loginUser);

    List<Response> findResponseBySurveyId(Integer surveyId);
}