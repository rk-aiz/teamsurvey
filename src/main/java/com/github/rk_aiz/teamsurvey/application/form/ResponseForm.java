package com.github.rk_aiz.teamsurvey.application.form;

import java.util.List;

import lombok.Data;

@Data
public class ResponseForm {
    private Integer surveyId;
    private String token;

    /** 各設問への回答詳細リスト */
    private List<ResponseDetailForm> details;
}