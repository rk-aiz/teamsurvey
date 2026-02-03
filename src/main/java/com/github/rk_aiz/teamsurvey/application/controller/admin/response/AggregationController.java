package com.github.rk_aiz.teamsurvey.application.controller.admin.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyResultService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Responseの一覧/詳細画面のコントローラー
 */
@Slf4j
@Controller
@RequestMapping("/admin/result")
@RequiredArgsConstructor
public class AggregationController {

    /** 定数 */
    private static final String MESSAGE = "message";
    private static final String REDIRECT_TO_LIST = "redirect:/admin/response/list";

    /** DI */
    private final SurveyService surveyService;
    private final SurveyResultService surveyResultService;
    private final SmartValidator validator;

    /**
     * 集計一覧画面を表示します
     */
    @GetMapping
    public String survey() {
        return "redirect:/admin/result/list";
    }

    /**
     * 集計一覧画面を表示します
     */
    @GetMapping("/list")
    public String list(@RequestParam(value = "id", required = false) Integer id, Model model) {

        // 全件取得（中央カラム用）
        model.addAttribute("results", surveyResultService.findAllSurveyAggregations());

        return "admin/result/list";
    }

}