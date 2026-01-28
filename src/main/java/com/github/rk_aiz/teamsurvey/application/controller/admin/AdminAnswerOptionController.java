package com.github.rk_aiz.teamsurvey.application.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.rk_aiz.teamsurvey.domain.service.AnswerOptionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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