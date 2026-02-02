package com.github.rk_aiz.teamsurvey.application.form;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;

import lombok.Data;

@Data
public class ResponseForm {

    /** 回答ID */
    private Integer responseId;
    /** アンケートID */
    private Integer surveyId;
    /** ユーザー名 */
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

    public Response toModel(Survey survey) {

        Response response = new Response();
        BeanUtils.copyProperties(this, response);

        
        response.setResponseDetails(this.getDetails()
                .stream()
                .map(detailForm -> detailForm.toModel(survey.getQuestionById(detailForm.getQuestionId())))
                .toList());

        return response;
    }
}