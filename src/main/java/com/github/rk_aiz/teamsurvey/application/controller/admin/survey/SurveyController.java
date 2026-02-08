package com.github.rk_aiz.teamsurvey.application.controller.admin.survey;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.form.QuestionForm;
import com.github.rk_aiz.teamsurvey.application.form.SurveyForm;
import com.github.rk_aiz.teamsurvey.application.validation.SurveyValidationGroup;
import com.github.rk_aiz.teamsurvey.domain.exception.ServiceRuleException;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.service.AnswerOptionService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyTargetGroupService;
import com.github.rk_aiz.teamsurvey.domain.service.UserGroupService;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Surveyの一覧/詳細/登録/編集画面のコントローラー
 * 対象Viewはresources/templates/admin/survey/*
 */
@Slf4j
@Controller
@RequestMapping("/admin/survey")
@RequiredArgsConstructor
public class SurveyController {

    /** 定数 */
    private static final String MESSAGE = "message";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String REDIRECT_TO_LIST = "redirect:/admin/survey/list";

    /** DI */
    private final SurveyService surveyService;
    private final UserGroupService userGroupService;
    private final AnswerOptionService answerOptionService;
    private final SurveyTargetGroupService surveyTargetGroupService;
    private final SmartValidator validator;

    /**
     * アンケートの一覧画面を表示します
     */
    @GetMapping
    public String survey() {
        return "redirect:/admin/survey/list";
    }

    /**
     * アンケートの一覧画面を表示します
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(value = "id", required = false) Integer id,
            Model model) {

        // 全件取得
        model.addAttribute(
                "surveys",
                this.surveyService.findAllSurveys());

        // IDが指定されている場合、詳細情報を取得（右カラム用）
        try {
            Optional.ofNullable(id)
                    .map(surveyService::findSurveyById)
                    .ifPresent(survey -> model.addAttribute("selectedSurvey", survey));
        } catch (IllegalArgumentException e) {
            // 見つからない場合、何もしない
        }

        return "admin/survey/list";
    }

    /**
     * 詳細画面を表示します（設問一覧も含む）
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {

        // アンケート情報の取得
        model.addAttribute("survey", surveyService.findSurveyById(id));
        model.addAttribute("userGroups", userGroupService.findAll());

        return "admin/survey/detail";
    }

    /**
     * 新規作成または編集画面を表示します（設問一覧も含む）
     * IDが指定されない場合は新規作成として扱います
     */
    @GetMapping(value = { "/edit", "/edit/{id}" })
    public String edit(@PathVariable(value = "id", required = false) Integer id, Model model) {

        if (id != null) {
            // 編集: 既存データを取得
            model.addAttribute("surveyForm",
                    SurveyForm.from(surveyService.findSurveyById(id), false));
        } else {
            // 新規: 空のFormを作成
            model.addAttribute("surveyForm", SurveyForm.from(
                    surveyService.getEmptySurvey(), true));
        }

        // 回答パターンの選択肢（ドロップダウン用）
        model.addAttribute("answerOptions", answerOptionService.findAll());
        model.addAttribute("userGroups", userGroupService.findAll());

        return "admin/survey/edit";
    }

    /**
     * アンケートをコピーして新規作成画面を表示します
     */
    @GetMapping("/copy/{id}")
    public String copy(@PathVariable("id") Integer id, Model model) {

        // アンケート情報の取得 -> クローン(ステータスはDRAFT) -> Formに変換
        model.addAttribute("surveyForm", SurveyForm.from(
                surveyService.findSurveyAsDraftCopy(id), true));

        // 回答パターンの選択肢（ドロップダウン用）
        model.addAttribute("answerOptions", answerOptionService.findAll());

        return "admin/survey/edit";
    }

    /**
     * Survey登録を実行します
     */
    @PostMapping("/save")
    public String save(
            @ModelAttribute SurveyForm surveyForm,
            @RequestParam(value = "groupIds", required = false) List<Integer> groupIds,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // ステータスに応じてバリデーショングループを切り替えてバリデーション
        validator.validate(
                surveyForm,
                bindingResult,
                SurveyValidationGroup.getValidationGroup(surveyForm.getStatus()));

        if (bindingResult.hasErrors()) {
            model.addAttribute("answerOptions", answerOptionService.findAll());
            model.addAttribute("userGroups", userGroupService.findAll());
            return "admin/survey/edit";
        }

        Survey newSurvey = surveyService.saveSurvey(surveyForm.toModel());

        if (surveyForm.isNew()) {
            redirectAttributes.addFlashAttribute(MESSAGE, "新しいアンケートが追加されました");
        } else {
            redirectAttributes.addFlashAttribute(MESSAGE, "アンケートを更新しました");
        }

        // アンケートと対象グループの紐づけ情報保存
        surveyTargetGroupService.save(newSurvey.getId(), groupIds);

        // PRGパターン
        return "redirect:/admin/survey/detail/" + newSurvey.getId();
    }

    /**
     * 設問追加ボタン押下時の処理
     * params = "addQuestion" でボタンのname属性を判定します
     */
    @PostMapping(value = "/save", params = "addQuestion")
    public String addQuestion(@ModelAttribute SurveyForm form, Model model) {
        form.getQuestionForms().add(new QuestionForm());
        model.addAttribute("answerOptions", answerOptionService.findAll());
        model.addAttribute("userGroups", userGroupService.findAll());
        return "admin/survey/edit";
    }

    /**
     * 設問削除ボタン押下時の処理
     * params = "removeQuestion" でボタンのname属性を判定します
     */
    @PostMapping(value = "/save", params = "removeQuestion")
    public String removeQuestion(@ModelAttribute SurveyForm form, @RequestParam("removeQuestion") int index,
            Model model) {
        // 指定されたインデックスの質問をリストから削除
        if (index >= 0 && index < form.getQuestionForms().size()) {
            form.getQuestionForms().remove(index);
        }
        model.addAttribute("answerOptions", answerOptionService.findAll());
        model.addAttribute("userGroups", userGroupService.findAll());
        return "admin/survey/edit";
    }

    /**
     * ステータスを変更します
     */
    @PostMapping("/status/{id}")
    public String changeStatus(
            @PathVariable("id") Integer id,
            @RequestParam("status") SurveyStatus status,
            @RequestHeader(value = "Referer", required = false) String referer,
            RedirectAttributes redirectAttributes) {

        try {
            if (surveyService.tryChangeStatusById(id, status))
                redirectAttributes.addFlashAttribute(MESSAGE, "ステータスを変更しました");
        } catch (ServiceRuleException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "システムエラーが発生しました。");
        }

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }

        return "redirect:/admin/survey/detail/" + id;
    }

    /**
     * 対象グループのみを更新します（詳細画面からの呼び出し）
     */
    @PostMapping("/target/{id}")
    public String updateTargetGroups(
            @PathVariable("id") Integer id,
            @RequestParam(value = "groupIds", required = false) List<Integer> groupIds,
            RedirectAttributes redirectAttributes) {

        // アンケートと対象グループの紐づけ情報保存
        surveyTargetGroupService.save(id, groupIds);

        redirectAttributes.addFlashAttribute(MESSAGE, "対象グループを更新しました");
        return "redirect:/admin/survey/detail/" + id;
    }
}