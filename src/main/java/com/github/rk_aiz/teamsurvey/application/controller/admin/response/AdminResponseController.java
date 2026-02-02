package com.github.rk_aiz.teamsurvey.application.controller.admin.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Responseの一覧/詳細画面のコントローラー
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminResponseController {

    /** 定数 */
    private static final String MESSAGE = "message";
    private static final String REDIRECT_TO_LIST = "redirect:/admin/response/list";

    /** DI */
    private final SurveyService surveyService;
    private final SmartValidator validator;

    /**
     * アンケートごとの回答数一覧画面を表示します
     */
    @GetMapping("/response")
    public String survey() {
        return "redirect:/admin/response/survey";
    }

    /**
     * アンケートごとの回答数一覧画面を表示します
     */
    @GetMapping("/response/survey")
    public String list(@RequestParam(value = "id", required = false) Integer id, Model model) {

        // 全件取得（中央カラム用）
        model.addAttribute("surveys",
                surveyService.findAvailableSurveys());

        // IDが指定されている場合、詳細情報を取得（右カラム用）
        if (id != null) {
            try {
                Survey selectedSurvey = surveyService.findSurveyById(id);
                model.addAttribute("selectedSurvey", selectedSurvey);
            } catch (IllegalArgumentException e) {
                // 指定されたIDが見つからない場合は、詳細を表示せずに一覧のみ表示を継続
                log.warn("指定されたアンケートIDが見つかりません: {}", id);
            }
        }

        return "admin/survey/list";
    }

}