package com.github.rk_aiz.teamsurvey.application.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.rk_aiz.teamsurvey.application.form.LoginForm;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AccountService accountService;

    /**
     * トップページ(ログイン画面)を表示します。
     * すでにログインしている場合はメニュー画面へリダイレクトします。
     */
    @GetMapping
    public String showLogin(
            @ModelAttribute LoginForm loginForm,
            Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }

        // システム管理者アカウントが1つも存在しない場合はセットアップ画面へリダイレクト
        if (!accountService.existsSystemAdmin()) {
            return "redirect:/setup";
        }

        return "login";
    }
}