package com.github.rk_aiz.teamsurvey.advice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;

@ControllerAdvice
public class GlobalControllerAdvice {

    // application.properties から app.name を取得（未設定時のデフォルトは "TeamSurvey"）
    @Value("${app.name:TeamSurvey}")
    private String appName;

    @ModelAttribute("appName")
    public String addAppNameToModel() {
        return appName;
    }

    /**
     * ログイン済みの場合、UserDetailsを "account" という名前でModelに追加します。
     */
    @ModelAttribute("account")
    public LoginUser addAccountToModel(@AuthenticationPrincipal LoginUser loginUser) {
        return loginUser;
    }
}