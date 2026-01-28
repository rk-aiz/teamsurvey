package com.github.rk_aiz.teamsurvey.application.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    /**
     * トップページ（ログイン画面）を表示します。
     * すでにログインしている場合はメニュー画面へリダイレクトします。
     */
    @GetMapping("/")
    public String index(Principal principal) {
        if (principal != null) {
            return "redirect:/menu";
        }
        return "index";
    }
}