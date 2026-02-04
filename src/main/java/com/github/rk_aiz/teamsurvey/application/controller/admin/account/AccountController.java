package com.github.rk_aiz.teamsurvey.application.controller.admin.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.rk_aiz.teamsurvey.application.form.AccountForm;
import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;
import com.github.rk_aiz.teamsurvey.domain.service.UserGroupService;

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
    private final UserGroupService userGroupService;
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
    public String list(
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "action", required = false) String action,
        @PageableDefault(size = 20) Pageable pageable, Model model) {

        // ページネーション付きで取得
        Page<LoginUser> users = accountService.findWithPaging(pageable);
        model.addAttribute("users", users);

        // モーダル表示用の処理
        if ("new".equals(action)) {
            // 新規作成
            AccountForm form = new AccountForm();
            form.setNew(true);
            model.addAttribute("accountForm", form);
            model.addAttribute("userGroups", this.userGroupService.findAll());
            model.addAttribute("showModal", true);
        } else if ("edit".equals(action) && username != null) {
            // 編集
            try {
                LoginUser user = accountService.findAccountByUsername(username);
                model.addAttribute("accountForm", AccountForm.from(user, false));
                model.addAttribute("showModal", true);
            } catch (IllegalArgumentException e) {
                log.warn("指定されたユーザーが見つかりません: {}", username);
            }
        }

        return "admin/account/list";
    }
}