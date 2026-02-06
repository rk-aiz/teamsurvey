package com.github.rk_aiz.teamsurvey.application.controller.admin.result;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyResultService;

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
    private final AccountService surveyService;
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
     * 詳細画面を表示します（設問一覧も含む）
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
    public ResponseEntity<byte[]> downloadCsv(@PathVariable("id") Integer id) {
        String csvData = surveyResultService.generateCsv(id);
        byte[] csvBytes = csvData.getBytes(StandardCharsets.UTF_8);
        // BOM付与 (Excelで文字化けしないように)
        byte[] csvBytesWithBom = new byte[csvBytes.length + 3];
        csvBytesWithBom[0] = (byte) 0xEF;
        csvBytesWithBom[1] = (byte) 0xBB;
        csvBytesWithBom[2] = (byte) 0xBF;
        System.arraycopy(csvBytes, 0, csvBytesWithBom, 3, csvBytes.length);

        String filename = "survey_result_" + id + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(csvBytesWithBom);
    }

}