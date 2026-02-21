package com.github.rk_aiz.teamsurvey.application.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    private static final String MESSAGE = "message";

    /**
     * アンケートに対する回答画面を表示します
     */
    @GetMapping("/{surveyId}")
    public String showResponsePage(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable("surveyId") Integer surveyId,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!surveyService.canResponseBySurveyid(surveyId, loginUser.getUsername())) {
            redirectAttributes.addFlashAttribute(
                    MESSAGE,
                    "このアンケートに回答する権限がありません。");
            return "redirect:/";
        }

        Survey survey = this.surveyService.findSurveyById(surveyId);

        model.addAttribute("survey", survey);
        model.addAttribute(
                "responseForm",
                ResponseForm.fromSurvey(survey, loginUser.getUsername()));

        return "user/response";
    }

    /**
     * アンケートに対する回答の登録処理を行います
     */
    @PostMapping("/submit")
    public String submitResponse(
            @Validated @ModelAttribute ResponseForm responseForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // バリデーションエラー時は回答画面に戻る
        if (bindingResult.hasErrors()) {
            return "user/response";
        }

        Survey survey = this.surveyService.findSurveyById(responseForm.getSurveyId());
        // 保存処理 (IDが発行される)
        boolean success = responseService.saveResponse(survey, responseForm.toModel(survey));
        if (!success) {
            redirectAttributes.addFlashAttribute(MESSAGE, "回答の登録に失敗しました。");
        } else {
            redirectAttributes.addFlashAttribute(MESSAGE, "回答を送信しました。");
        }
        // ホームへリダイレクト
        return "redirect:/";
    }
}
