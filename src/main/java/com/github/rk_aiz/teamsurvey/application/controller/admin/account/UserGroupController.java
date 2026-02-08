package com.github.rk_aiz.teamsurvey.application.controller.admin.account;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.form.UserGroupForm;
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
     * ユーザーグループ情報を保存します TODO : システムグループ関連の処理、UIの変更など
     */
    @PostMapping("/save")
    public String save(
            @Validated @ModelAttribute UserGroupForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            @PageableDefault(size = 20) Pageable pageable) {

        if (result.hasErrors()) {
            model.addAttribute("users", accountService.findWithPaging(pageable));
            model.addAttribute("userGroups", userGroupService.findAll());
            return "admin/account/list";
        }

        try {
            if (userGroupService.save(form.toModel())) {
                redirectAttributes.addFlashAttribute(MESSAGE, "グループを保存しました。");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "保存に失敗しました。");
            }
        } catch (DuplicateKeyException e) {
            result.rejectValue("groupName", "error.groupName", e.getMessage());
            model.addAttribute("users", accountService.findWithPaging(pageable));
            model.addAttribute("userGroups", userGroupService.findAll());
            return "admin/account/list";
        } catch (Exception e) {
            log.error("グループ保存エラー", e);
            redirectAttributes.addFlashAttribute("errorMessage", "システムエラーが発生しました。");
        }
        return REDIRECT_TO_LIST;
    }

    /**
     * ユーザーグループを削除します
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer groupId, RedirectAttributes redirectAttributes) {
        if (userGroupService.delete(groupId)) {
            redirectAttributes.addFlashAttribute(MESSAGE, "グループを削除しました。");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "削除に失敗しました。");
        }
        return REDIRECT_TO_LIST;
    }

}