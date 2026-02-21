package com.github.rk_aiz.teamsurvey.application.controller.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
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
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final SurveyService surveyService;
    private final SurveyResultService surveyResultService;
    private final AccountService accountService;

    @GetMapping
    public String showHome(
            @AuthenticationPrincipal LoginUser loginUser,
            @PageableDefault(size = 10) Pageable pageable, Model model) {

        List<Survey> surveys = surveyService.findAvailableSurveysByUsername(loginUser.getUsername());
        model.addAttribute("surveys", surveys);

        UserAccount account = accountService
                .findAccountByUsername(loginUser.getUsername())
                .orElseThrow(() -> new SystemCriticalException("ログイン中のユーザー情報が取得できません: " + loginUser.getUsername()));

        Page<SurveyAggregation> aggregations = surveyResultService.findWithPagingByUserGroups(
                pageable,
                account.assignedGroups());

        model.addAttribute("surveyAggregations", aggregations);

        return "user/home";
    }
}
