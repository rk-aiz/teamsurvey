package com.github.rk_aiz.teamsurvey.application.controller.admin;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.form.QuestionForm;
import com.github.rk_aiz.teamsurvey.application.form.SurveyForm;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.service.AnswerOptionService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;
import com.github.rk_aiz.teamsurvey.presentation.validation.SurveyValidationGroup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Surveyの一覧/詳細/登録/編集画面のコントローラー
 * 対象Viewはresources/templates/admin/survey/*
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminSurveyController {

    /** 定数 */
    private static final String MESSAGE = "message";
    private static final String REDIRECT_TO_LIST = "redirect:/admin/survey/list";

    /** DI */
    private final SurveyService surveyService;
    private final AnswerOptionService answerOptionService;
    private final SmartValidator validator;

    /** デフォルトの締め切り日数（application.propertiesから取得） */
    @Value("${app.survey.default-deadline-days:30}")
    private int defaultDeadlineDays;

    /**
     * アンケートの一覧画面を表示します
     */
    @GetMapping("/survey/list")
    public String list(@RequestParam(value = "id", required = false) Integer id, Model model) {

        // 全件取得（中央カラム用）
        model.addAttribute("surveys",
                surveyService
                        .findAllSurveys());

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

    /**
     * 詳細画面を表示します（設問一覧も含む）
     */
    @GetMapping("/survey/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {

        // アンケート情報の取得
        Survey survey = surveyService.findSurveyById(id);
        model.addAttribute("survey", survey);
        model.addAttribute("surveyId", id);

        return "admin/survey/detail";
    }

    /**
     * 編集画面を表示します（設問一覧も含む）
     */
    @GetMapping("/survey/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {

        // Modelに追加 <- Formに変換 <- アンケート情報(エンティティ)の取得 <- id
        model.addAttribute("surveyForm",
                SurveyForm.from(surveyService.findSurveyById(id), false));

        // 回答パターンの選択肢（ドロップダウン用）
        model.addAttribute("answerOptions", answerOptionService.findAll());

        return "admin/survey/edit";
    }

    /**
     * アンケートをコピーして新規作成画面を表示します
     */
    @GetMapping("/survey/copy/{id}")
    public String copy(@PathVariable("id") Integer id, Model model) {

        // アンケート情報の取得 -> クローン(ステータスはDRAFT) -> Formに変換
        model.addAttribute("surveyForm", SurveyForm.from(
                surveyService.findSurveyAsDraftCopy(id), true));

        // 回答パターンの選択肢（ドロップダウン用）
        model.addAttribute("answerOptions", answerOptionService.findAll());

        return "admin/survey/edit";
    }

    /**
     * 新規登録画面を表示します
     */
    @GetMapping("/survey/new")
    public String form(
            @ModelAttribute SurveyForm form,
            Model model) {
        form.setNew(true);
        // デフォルトの締め切り日時を設定（現在日時 + 設定された日数）
        form.setDeadline(LocalDateTime.now().plusDays(defaultDeadlineDays));
        return "admin/survey/new";
    }

    /**
     * 新規作成(Step1)からの登録処理
     * タイトル等を保存し、編集画面(Step2)へ遷移します
     */
    @PostMapping("/survey/create")
    public String create(
            @Validated @ModelAttribute SurveyForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // バリデーションエラー時は新規作成画面に戻る
        if (bindingResult.hasErrors()) {
            return "admin/survey/new";
        }

        // 保存処理 (IDが発行される)
        Survey savedSurvey = surveyService.saveSurvey(form.toModel());

        redirectAttributes.addFlashAttribute(MESSAGE, "下書きを作成しました。続けて設問を編集してください。");
        // 編集画面へリダイレクト
        return "redirect:/admin/survey/edit/" + savedSurvey.getSurveyId();
    }

    /**
     * Survey登録を実行します
     */
    @PostMapping("/survey/save")
    public String save(
            @ModelAttribute SurveyForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // ステータスに応じてバリデーショングループを切り替えてバリデーション
        validator.validate(
                form,
                bindingResult,
                SurveyValidationGroup.getValidationGroup(form.getStatus()));

        if (bindingResult.hasErrors()) {
            model.addAttribute("answerOptions", answerOptionService.findAll());
            return "admin/survey/edit/";
        }

        // サービス層で保存処理（新規・更新・セキュリティチェック・削除同期すべて含む）
        surveyService.saveSurvey(form.toModel());

        if (form.isNew()) {
            redirectAttributes.addFlashAttribute(MESSAGE, "新しいアンケートが追加されました");
        } else {
            redirectAttributes.addFlashAttribute(MESSAGE, "アンケートを更新しました");
        }
        // PRGパターン
        return "redirect:/admin/survey/detail/" + form.getSurveyId();
    }

    /**
     * 設問追加ボタン押下時の処理
     * params = "addQuestion" でボタンのname属性を判定します
     */
    @PostMapping(value = "/survey/save", params = "addQuestion")
    public String addQuestion(@ModelAttribute SurveyForm form, Model model) {
        form.getQuestionForms().add(new QuestionForm());
        model.addAttribute("answerOptions", answerOptionService.findAll());
        return "admin/survey/edit";
    }

    /**
     * 設問削除ボタン押下時の処理
     * params = "removeQuestion" でボタンのname属性を判定します
     */
    @PostMapping(value = "/survey/save", params = "removeQuestion")
    public String removeQuestion(@ModelAttribute SurveyForm form, @RequestParam("removeQuestion") int index,
            Model model) {
        // 指定されたインデックスの質問をリストから削除
        if (index >= 0 && index < form.getQuestionForms().size()) {
            form.getQuestionForms().remove(index);
        }
        model.addAttribute("answerPatterns", answerOptionService.findAll());
        return "admin/survey/edit";
    }

    /**
     * ステータスを変更します
     */
    @PostMapping("/survey/status/{id}")
    public String changeStatus(
            @PathVariable("id") Integer id,
            @RequestParam("status") SurveyStatus status,
            RedirectAttributes redirectAttributes) {

        try {
            surveyService.tryChangeStatusById(id, status);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.getMessage());
            return "redirect:/admin/survey/detail/" + id;
        }

        redirectAttributes.addFlashAttribute(MESSAGE, "ステータスを変更しました");
        return "redirect:/admin/survey/detail/" + id;
    }

    @GetMapping("/survey/target/")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

}