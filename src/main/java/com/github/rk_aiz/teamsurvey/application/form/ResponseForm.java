package com.github.rk_aiz.teamsurvey.application.form;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;

import lombok.Data;

@Data
public class ResponseForm {
    private Integer surveyId;
    private String username;

    /** 各設問への回答詳細リスト */
    private List<ResponseDetailForm> details;

    public static ResponseForm fromSurvey(Survey survey, LoginUser loginUser) {

        ResponseForm form = new ResponseForm();
        List<ResponseDetailForm> details = survey
                .getQuestions()
                .stream()
                .map(ResponseDetailForm::fromQuestion)
                .toList();

        form.setSurveyId(survey.getSurveyId());
        form.setUsername(loginUser.getUsername());
        form.setDetails(details);

        return form;
    }
}