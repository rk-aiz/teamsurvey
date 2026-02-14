package com.github.rk_aiz.teamsurvey.application.controller.admin.survey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.rk_aiz.teamsurvey.application.constant.WebConst;
import com.github.rk_aiz.teamsurvey.application.form.AnswerOptionForm;
import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.service.AnswerOptionService;
import com.github.rk_aiz.teamsurvey.util.ServletUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/pattern/fragment")
@RequiredArgsConstructor
public class AnswerOptionController {

    /** 定数 */
    private static final ServletUtils.Fragment PATTERN_FRAGMENTS = ServletUtils
            .fragment("admin/survey/pattern_fragments");

    /** DI */
    private final AnswerOptionService answerOptionService;

    /**
     * 回答パターン一覧フラグメントを取得
     */
    @GetMapping("/list")
    public String getListFragment(Model model) {
        model.addAttribute("answerOptions", answerOptionService.findAll());
        return PATTERN_FRAGMENTS.target("list");
    }

    /**
     * 回答パターン編集フォームフラグメントを取得
     */
    @GetMapping("/form")
    public String getFormFragment(
            @RequestParam(name = "id", required = false) Integer id,
            Model model) {

        AnswerOptionForm form = new AnswerOptionForm();
        if (id != null) {
            AnswerOption answerOption = answerOptionService.findAnswerOptionById(id);

            if (answerOption != null) {
                // Entity -> Form 変換 (簡易実装: 本来はFormクラスに変換メソッドを持たせるかMapperを使う)
                form = AnswerOptionForm.from(answerOption);
            }
        }

        model.addAttribute("answerOptionForm", form);
        return PATTERN_FRAGMENTS.target("form");
    }

    /**
     * 回答パターン保存処理
     * 成功時は一覧フラグメント、失敗時はエラー付きフォームフラグメントを返す
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<Map<String, String>> save(
            @Validated @RequestBody AnswerOptionForm form,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        answerOptionService.save(form.toModel());

        return ResponseEntity.ok(Map.of(WebConst.MESSAGE, "回答パターンを保存しました。"));
    }

    /**
     * AJAX: 回答パターン一覧をJSONで取得 (ドロップダウン更新用)
     */
    @GetMapping("/list-json")
    @ResponseBody
    public List<AnswerOption> getListJson() {
        return answerOptionService.findAll();
    }
}