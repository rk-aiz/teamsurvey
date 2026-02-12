package com.github.rk_aiz.teamsurvey.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.domain.service.AccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/setup")
@RequiredArgsConstructor
public class SetupController {

    private final AccountService accountService;

    @GetMapping
    public String setup() {
        // 安全策: すでにアカウントがある場合はセットアップ画面にアクセスさせない
        if (accountService.existsSystemAdmin()) {
            return "redirect:/auth";
        }
        return "setup";
    }

    @PostMapping
    public String createFirstAdmin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            RedirectAttributes redirectAttributes) {

        // 安全策: 二重送信などでアカウントが既にできている場合
        if (accountService.existsSystemAdmin()) {
            return "redirect:/auth";
        }

        // 管理者権限(ADMIN)を持つユーザーを作成するメソッドを呼び出す
        accountService.createInitialAdmin(username, password);

        redirectAttributes.addFlashAttribute("message", "管理者アカウントを作成しました。ログインしてください。");
        return "redirect:/auth";
    }
}
