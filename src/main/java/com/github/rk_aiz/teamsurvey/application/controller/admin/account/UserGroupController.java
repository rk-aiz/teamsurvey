package com.github.rk_aiz.teamsurvey.application.controller.admin.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.form.AccountForm;
import com.github.rk_aiz.teamsurvey.application.form.UserGroupForm;
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

    /**
     * ユーザーグループ情報を保存します
     */
    @PostMapping("/save")
    public String save(
            @Validated @ModelAttribute UserGroupForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            @PageableDefault(size = 20) Pageable pageable) {

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }
            model.addAttribute("users", accountService.findWithPaging(pageable));
            model.addAttribute("userGroups", userGroupService.findAll());
            model.addAttribute("showGroupModal", true);
            return "admin/account/list";
        }

        if (userGroupService.save(form.toModel())) {
            redirectAttributes.addFlashAttribute(MESSAGE, "グループを保存しました。");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "保存に失敗しました。");
        }

        return REDIRECT_TO_LIST;
    }

}