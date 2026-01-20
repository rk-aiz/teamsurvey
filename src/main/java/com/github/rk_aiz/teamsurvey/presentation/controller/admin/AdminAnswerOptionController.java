package com.github.rk_aiz.teamsurvey.presentation.controller.admin;

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

import com.github.rk_aiz.teamsurvey.application.service.AnswerOptionService;
import com.github.rk_aiz.teamsurvey.application.service.SurveyService;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;
import com.github.rk_aiz.teamsurvey.presentation.form.QuestionForm;
import com.github.rk_aiz.teamsurvey.presentation.form.SurveyForm;
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
public class AdminAnswerOptionController {

    /** 定数 */
    private static final String MESSAGE = "message";

    /** DI */
    private final AnswerOptionService answerOptionService;
    private final SmartValidator validator;

    @GetMapping("/pattern/list")
    public String list(Model model) {

        model.addAttribute("answerOptions",
                answerOptionService
                        .findAll());

        return "admin/pattern/list";
    }
    

}