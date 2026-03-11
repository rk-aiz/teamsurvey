package com.github.rk_aiz.teamsurvey.application.controller.admin.account;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.form.UserGroupForm;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;
import com.github.rk_aiz.teamsurvey.domain.service.UserGroupService;
import com.github.rk_aiz.teamsurvey.exception.ServiceRuleException;
import com.github.rk_aiz.teamsurvey.util.ServletUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * UserGroupの一覧/詳細画面のコントローラー
 */
@Slf4j
@Controller
@RequestMapping("/admin/group")
@RequiredArgsConstructor
public class UserGroupController {

    /** 定数 */
    private static final String MESSAGE = "message";
    private static final String ERROR_MESSAGE = "message";
    private static final String USERS = "users";
    private static final String USER_GROUPS = "userGroups";
    private static final String ACCOUNT_LIST = "admin/account/list";

    /** DI */
    private final AccountService accountService;
    private final UserGroupService userGroupService;

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
            model.addAttribute(USERS, accountService.findWithPaging(pageable));
            model.addAttribute(USER_GROUPS, userGroupService.findAll());
            return ACCOUNT_LIST;
        }

        try {
            userGroupService.save(form.toModel());
            redirectAttributes.addFlashAttribute(MESSAGE, "グループを保存しました。");
        } catch (DuplicateKeyException e) {
            result.rejectValue("groupName", "error.groupName", e.getLocalizedMessage());
            model.addAttribute(USERS, accountService.findWithPaging(pageable));
            model.addAttribute(USER_GROUPS, userGroupService.findAll());
            return ACCOUNT_LIST;
        } catch (ServiceRuleException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getLocalizedMessage());
            model.addAttribute(USERS, accountService.findWithPaging(pageable));
            model.addAttribute(USER_GROUPS, userGroupService.findAll());
            return ACCOUNT_LIST;
        } catch (Exception e) {
            log.error("グループ保存エラー", e);
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "システムエラーが発生しました。");
        }
        return ServletUtils.redirect(ACCOUNT_LIST);
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
        return ServletUtils.redirect(ACCOUNT_LIST);
    }

}