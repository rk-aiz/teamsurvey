package com.github.rk_aiz.teamsurvey.application.controller.admin.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Responseの一覧/詳細画面のコントローラー
 */
@Slf4j
@Controller
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class AccountController {

    /** 定数 */
    private static final String MESSAGE = "message";
    private static final String REDIRECT_TO_LIST = "redirect:/admin/account/list";

    /** DI */
    private final AccountService accountService;
    private final SmartValidator validator;

    /**
     * 集計一覧画面を表示します
     */
    @GetMapping
    public String survey() {
        return "redirect:/admin/account/list";
    }

    /**
     * 集計一覧画面を表示します
     */
    @GetMapping("/list")
    public String list(@PageableDefault(size = 20) Pageable pageable, Model model) {

        // ページネーション付きで取得
        Page<LoginUser> users = accountService.findWithPaging(pageable);
        model.addAttribute("users", users);

        return "admin/account/list";
    }
}