package com.github.rk_aiz.teamsurvey.application.controller.admin.survey;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.form.AnswerOptionForm;
import com.github.rk_aiz.teamsurvey.application.form.SurveyForm;
import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.service.AnswerOptionService;
import com.github.rk_aiz.teamsurvey.domain.service.UserGroupService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/pattern")
@RequiredArgsConstructor
public class AnswerOptionController {

    /** 定数 */
    private static final String MESSAGE = "message";

    /** DI */
    private final AnswerOptionService answerOptionService;
    private final UserGroupService userGroupService;
    private final SmartValidator validator;

    /**
     * 回答パターン保存を実行します
     */
    @PostMapping("/save")
    public String save(
            @ModelAttribute AnswerOptionForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("answerOptions", answerOptionService.findAll());
            return "admin/survey/edit";
        }

        // サービス層で保存処理
        answerOptionService.save(form.toModel());

        if (form.getAnswerOptionId() == null) {
            redirectAttributes.addFlashAttribute(MESSAGE, 
                "新しい回答パターンが追加されました");
        } else {
            redirectAttributes.addFlashAttribute(MESSAGE, 
                "回答パターンが更新されました");
        }

        return "admin/survey/edit";
    }

    /**
     * 回答パターン追加ボタン押下時の処理
     * params = "addAnswerOption" でボタンのname属性を判定します
     */
    @PostMapping(value = "/save", params = "addAnswerOption")
    public String addQuestion(
            @ModelAttribute AnswerOptionForm form, Model model) {
        List<AnswerOption> answerOptions = answerOptionService.findAll();
        answerOptions.add(new AnswerOption());
        model.addAttribute("answerOptions", answerOptions);
        model.addAttribute("userGroups", userGroupService.findAll());
        return "admin/survey/edit";
    }

    /**
     * 回答パターン削除ボタン押下時の処理
     * params = "removeAnswerOption" でボタンのname属性を判定します
     */
    @PostMapping(value = "/save", params = "removeAnswerOption")
    public String removeQuestion(@ModelAttribute SurveyForm form, @RequestParam("removeQuestion") int index,
            Model model) {
        // 指定されたインデックスの回答パターンを削除
        answerOptionService.remove(index);
        model.addAttribute("answerOptions", answerOptionService.findAll());
        model.addAttribute("userGroups", userGroupService.findAll());
        return "admin/survey/edit";
    }

    // --- 以下、非同期(AJAX)処理用のメソッド ---

    /**
     * AJAX: 回答パターン一覧フラグメントを取得
     */
    @GetMapping("/fragment/list")
    public String getListFragment(Model model) {
        model.addAttribute("answerOptions", answerOptionService.findAll());
        return "admin/survey/pattern_fragments :: list";
    }

    /**
     * AJAX: 回答パターン編集フォームフラグメントを取得
     */
    @GetMapping("/fragment/form")
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
                // itemsの変換が必要な場合はここで行う
            }
        }
        model.addAttribute("answerOptionForm", form);
        return "admin/survey/pattern_fragments :: form";
    }

    /**
     * AJAX: 回答パターン保存処理
     * 成功時は一覧フラグメント、失敗時はエラー付きフォームフラグメントを返す
     */
    @PostMapping("/fragment/save")
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