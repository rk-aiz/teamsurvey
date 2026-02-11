package com.github.rk_aiz.teamsurvey.application.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.rk_aiz.teamsurvey.application.form.LoginForm;

@Controller
@RequestMapping("/auth")
public class LoginController {

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
        return "login";
    }
}