package com.github.rk_aiz.teamsurvey.application.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.form.ResponseForm;
import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.service.ResponseService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/response")
@RequiredArgsConstructor
public class ResponseController {

    private final SurveyService surveyService;
    private final ResponseService responseService;

    @GetMapping("/{surveyId}")
    public String showResponsePage(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable("surveyId") Integer surveyId,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!surveyService.canResponseBySurveyid(surveyId, loginUser.getUsername())) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "このアンケートに回答する権限がありません。");
            return "redirect:/";
        }

        Survey survey = this.surveyService.findSurveyById(surveyId);

        model.addAttribute("survey", survey);
        model.addAttribute("responseForm", ResponseForm.fromSurvey(survey, loginUser));

        return "user/response";
    }
}
