package com.github.rk_aiz.teamsurvey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    // application.properties から app.name を取得（未設定時のデフォルトは "TeamSurvey"）
    @Value("${app.name:TeamSurvey}")
    private String appName;

    @ModelAttribute("appName")
    public String appName() {
        return appName;
    }
}