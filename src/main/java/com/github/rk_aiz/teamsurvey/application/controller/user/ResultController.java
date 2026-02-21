package com.github.rk_aiz.teamsurvey.application.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyResultService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;
import com.github.rk_aiz.teamsurvey.exception.SystemCriticalException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/result")
@RequiredArgsConstructor
public class ResultController {

    private final SurveyService surveyService;
    private final SurveyResultService surveyResultService;
    private final AccountService accountService;

    @GetMapping("/{id}")
    public String detail(
            @PathVariable("id") Integer id, 
            @AuthenticationPrincipal LoginUser loginUser,
            Model model) {

        UserAccount account = accountService
                .findAccountByUsername(loginUser.getUsername())
                .orElseThrow(() -> new SystemCriticalException("ログイン中のユーザー情報が取得できません: " + loginUser.getUsername()));

        // アンケート情報の取得
        SurveyAggregation aggregation = 
            surveyResultService.findSurveyAggregationByIdAndAccount(
                    id,
                    account);

        model.addAttribute("aggregation", aggregation);

        return "user/result";
    }
}
