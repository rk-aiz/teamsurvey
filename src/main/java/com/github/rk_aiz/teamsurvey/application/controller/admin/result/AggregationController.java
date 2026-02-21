package com.github.rk_aiz.teamsurvey.application.controller.admin.result;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyResultService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;

import jakarta.servlet.http.HttpServletResponse;
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
    public String list(
            Model model) {

        // 全件取得
        model.addAttribute("aggregations",
                surveyResultService.findAllSurveyAggregations());

        return "admin/result/list";
    }

    /**
     * 詳細画面を表示します(設問一覧も含む)
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {

        // アンケート情報の取得
        SurveyAggregation aggregation = surveyResultService.findSurveyAggregationById(id);

        model.addAttribute("aggregation", aggregation);

        return "admin/result/detail";
    }

    /**
     * CSVダウンロード
     */
    @GetMapping("/download/{id}")
    public void downloadCsv(
        @PathVariable("id") Integer id,
        HttpServletResponse response) throws IOException {

        Survey survey = surveyService.findSurveyById(id);

        // 1. レスポンスヘッダーの設定（ダウンロード）
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader(
                "Content-Disposition",
                        String.format(
                                "attachment; filename=\"survey%04d_result.csv\"",
                                        survey.getId()
                                )
                );

        // 2. 日本のExcel対策（BOMを付与して文字化けを防ぐ）
        PrintWriter writer = response.getWriter();
        writer.write('\ufeff'); 

        // 3. サービスを呼び出す（先ほどのリポジトリ隠ぺいパターン
        surveyResultService.exportToCsv(id, writer);
        
        writer.flush();
    }

}