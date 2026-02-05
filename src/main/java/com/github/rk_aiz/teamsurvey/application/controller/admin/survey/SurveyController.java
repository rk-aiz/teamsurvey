package com.github.rk_aiz.teamsurvey.application.controller.admin.survey;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.form.QuestionForm;
import com.github.rk_aiz.teamsurvey.application.form.SurveyForm;
import com.github.rk_aiz.teamsurvey.application.validation.SurveyValidationGroup;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.service.AnswerOptionService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;
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
    private static final String REDIRECT_TO_LIST = "redirect:/admin/survey/list";

    /** DI */
    private final SurveyService surveyService;
    private final UserGroupService userGroupService;
    private final AnswerOptionService answerOptionService;
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
            @RequestParam(value = "status", required = false) SurveyStatus status,
            Model model) {

        // 全件取得
        List<Survey> allSurveys = surveyService.findAllSurveys();
        List<Survey> displayedSurveys;

        if (status != null) {
            displayedSurveys = allSurveys.stream()
                    .filter(s -> s.getStatus() == status)
                    .collect(Collectors.toList());
        } else {
            displayedSurveys = allSurveys;
        }

        model.addAttribute("surveys", displayedSurveys);
        model.addAttribute("currentStatus", status);

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
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {

        // アンケート情報の取得
        model.addAttribute("survey", surveyService.findSurveyById(id));
        model.addAttribute("userGroups", userGroupService.findAll());

        return "admin/survey/detail";
    }

    /**
     * 編集画面を表示します（設問一覧も含む）
     */
    @GetMapping("/edit/{id}")
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
     * 新規作成
     * 空のFormを作成し、編集画面へ遷移します
     */
    @GetMapping("/new")
    public String create(
            Model model) {

        // アンケート情報の取得 -> クローン(ステータスはDRAFT) -> Formに変換
        model.addAttribute("surveyForm", SurveyForm.from(
                surveyService.getEmptySurvey(), true));

        // 回答パターンの選択肢（ドロップダウン用）
        model.addAttribute("answerOptions", answerOptionService.findAll());

        // 編集画面へリダイレクト
        return "/admin/survey/edit";
    }

    /**
     * Survey登録を実行します
     */
    @PostMapping("/save")
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
            model.addAttribute("userGroups", userGroupService.findAll());
            return "admin/survey/edit";
        }

        // サービス層で保存処理（新規・更新・セキュリティチェック・削除同期すべて含む）
        Survey newSurvey = surveyService.saveSurvey(form.toModel());

        if (form.isNew()) {
            redirectAttributes.addFlashAttribute(MESSAGE, "新しいアンケートが追加されました");
        } else {
            redirectAttributes.addFlashAttribute(MESSAGE, "アンケートを更新しました");
        }
        // PRGパターン
        return "redirect:/admin/survey/detail/" + newSurvey.getSurveyId();
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

    /**
     * 対象グループのみを更新します（詳細画面からの呼び出し）
     */
    @PostMapping("/target/{id}")
    public String updateTargetGroups(
            @PathVariable("id") Integer id,
            @RequestParam(value = "groupIds", required = false) List<Integer> groupIds,
            RedirectAttributes redirectAttributes) {

        Survey survey = surveyService.findSurveyById(id);
        List<UserGroup> allGroups = userGroupService.findAll();
        List<UserGroup> newTargetGroups = new ArrayList<>();

        if (groupIds != null) {
            for (UserGroup group : allGroups) {
                if (groupIds.contains(group.getGroupId())) {
                    newTargetGroups.add(group);
                }
            }
        }
        survey.setTargetGroups(newTargetGroups);
        surveyService.saveSurvey(survey);

        redirectAttributes.addFlashAttribute(MESSAGE, "対象グループを更新しました");
        return "redirect:/admin/survey/detail/" + id;
    }

}