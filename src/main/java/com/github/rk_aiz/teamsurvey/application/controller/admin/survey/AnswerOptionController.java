package com.github.rk_aiz.teamsurvey.application.controller.admin.survey;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.rk_aiz.teamsurvey.application.form.AnswerOptionForm;
import com.github.rk_aiz.teamsurvey.application.form.OptionItemForm;
import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.service.AnswerOptionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/pattern/fragment")
@RequiredArgsConstructor
public class AnswerOptionController {

    /** 定数 */
    private static final String MESSAGE = "message";

    /** DI */
    private final AnswerOptionService answerOptionService;
    private final SmartValidator validator;

    /**
     * 回答パターン一覧フラグメントを取得
     */
    @GetMapping("/list")
    public String getListFragment(Model model) {
        model.addAttribute("answerOptions", answerOptionService.findAll());
        return "admin/survey/pattern_fragments :: list";
    }

    /**
     * 回答パターン編集フォームフラグメントを取得
     */
    @GetMapping("/form")
    public String getFormFragment(@RequestParam(required = false) Integer id, Model model) {
        AnswerOptionForm form = new AnswerOptionForm();
        if (id != null) {
            // 簡易検索 (本来はService.findByIdを使用すべき)
            AnswerOption entity = answerOptionService.findAll().stream()
                    .filter(opt -> opt.getAnswerOptionId().equals(id))
                    .findFirst()
                    .orElse(null);
            
            if (entity != null) {
                // Entity -> Form 変換 (簡易実装: 本来はFormクラスに変換メソッドを持たせるかMapperを使う)
                form.setAnswerOptionId(entity.getAnswerOptionId());
                form.setName(entity.getName());
                
                // itemsの変換
                if (entity.getItems() != null) {
                    List<OptionItemForm> formItems = new ArrayList<>();
                    for (AnswerOption.OptionItem item : entity.getItems()) {
                        OptionItemForm formItem = new OptionItemForm();
                        formItem.setItemText(item.getItemText());
                        formItems.add(formItem);
                    }
                    form.setItems(formItems);
                }
            }
        }
        model.addAttribute("answerOptionForm", form);
        return "admin/survey/pattern_fragments :: form";
    }

    /**
     * 回答パターン保存処理
     * 成功時は一覧フラグメント、失敗時はエラー付きフォームフラグメントを返す
     */
    @PostMapping("/save")
    public String saveAjax(
            @Validated @ModelAttribute AnswerOptionForm form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "admin/survey/pattern_fragments :: form";
        }

        answerOptionService.save(form.toModel());
        
        // 保存成功時は一覧を返す
        model.addAttribute("answerOptions", answerOptionService.findAll());
        return "admin/survey/pattern_fragments :: list";
    }
}