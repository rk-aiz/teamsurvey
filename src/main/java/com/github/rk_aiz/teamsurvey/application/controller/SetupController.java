package com.github.rk_aiz.teamsurvey.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.form.AccountForm;
import com.github.rk_aiz.teamsurvey.application.mapper.AccountFormMapper;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/setup")
@RequiredArgsConstructor
public class SetupController {

    private final AccountService accountService;
    private final AccountFormMapper accountFormMapper;

    @GetMapping
    public String setup(Model model) {
        // 安全策: すでにアカウントがある場合はセットアップ画面にアクセスさせない
        if (accountService.existsAdmin()) {
            return "redirect:/";
        }

        model.addAttribute("accountForm", AccountForm.empty());
        return "setup";
    }

    @PostMapping
    public String createFirstAdmin(
            @Validated @ModelAttribute AccountForm accountForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // 安全策: 二重送信などでアカウントが既にできている場合
        if (accountService.existsAdmin()) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            return "setup";
        }

        // 管理者権限(ADMIN)を持つユーザーを作成するメソッドを呼び出す
        accountService.createInitialAdmin(accountFormMapper.toModel(accountForm));

        redirectAttributes.addFlashAttribute("message", "管理者アカウントを作成しました。ログインしてください。");
        return "redirect:/auth";
    }
}
