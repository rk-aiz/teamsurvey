package com.github.rk_aiz.teamsurvey.application.controller.user;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.service.ResponseService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final SurveyService surveyService;

    @GetMapping
    public String showHome(
            @AuthenticationPrincipal LoginUser loginUser,
            Model model) {

        if (loginUser == null) {
        	//TODO : エラーメッセージ
        	return "/";
        }
        
        List<Survey> surveys = this.surveyService.findAvailableSurveysByUsername(loginUser.getUsername());
        model.addAttribute("surveys", surveys);
        
        return "user/home";
    }

}
