package com.github.rk_aiz.teamsurvey.application.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
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

        if (loginUser != null) {
            model.addAttribute("surveys",
                    this.surveyService.findSurveyByUsername(loginUser.getUsername()));
        }

        // IDが指定されている場合、詳細情報を取得（右カラム用）
        // if (id != null) {
        // try {
        // Survey selectedSurvey = surveyService.findSurveyById(id);
        // model.addAttribute("selectedSurvey", selectedSurvey);
        // } catch (IllegalArgumentException e) {
        // // 指定されたIDが見つからない場合は、詳細を表示せずに一覧のみ表示を継続
        // log.warn("指定されたアンケートIDが見つかりません: {}", id);
        // }
        // }
        return "user/home";
    }

}
