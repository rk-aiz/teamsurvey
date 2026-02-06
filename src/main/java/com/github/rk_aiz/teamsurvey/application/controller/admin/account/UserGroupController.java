package com.github.rk_aiz.teamsurvey.application.controller.admin.account;

import org.springframework.stereotype.Controller;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.rk_aiz.teamsurvey.domain.service.AccountService;
import com.github.rk_aiz.teamsurvey.domain.service.UserGroupService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Responseの一覧/詳細画面のコントローラー
 */
@Slf4j
@Controller
@RequestMapping("/admin/group")
@RequiredArgsConstructor
public class UserGroupController {

    /** 定数 */
    private static final String MESSAGE = "message";
    private static final String REDIRECT_TO_LIST = "redirect:/admin/account/list";

    /** DI */
    private final AccountService accountService;
    private final UserGroupService userGroupService;
    private final SmartValidator validator;

}