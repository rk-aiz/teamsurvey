package com.github.rk_aiz.teamsurvey.application.controller.admin.account;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        Page<LoginUser> users = this.accountService.findWithPaging(pageable);
        model.addAttribute("users", users);
        model.addAttribute("userGroups", this.userGroupService.findAll());

        // モーダル表示用の処理
        if ("new".equals(action)) {
            // 新規作成
            AccountForm form = new AccountForm();
            form.setNew(true);
            model.addAttribute("accountForm", form);
            model.addAttribute("showModal", true);
        } else if ("edit".equals(action) && username != null) {
            // 編集
            try {
                model.addAttribute("accountForm",
                        AccountForm.from(this.accountService.findAccountByUsername(username), false));
                model.addAttribute("isLastAdmin", accountService.isLastAdmin(username));
                model.addAttribute("showModal", true);
            } catch (IllegalArgumentException | NoSuchElementException e) {
                log.warn("指定されたユーザーが見つかりません: {}", username);
            }
        }

        return "admin/account/list";
    }

    /**
     * アカウント情報を保存（新規登録・更新）します
     */
    @PostMapping("/save")
    public String save(
            @Validated @ModelAttribute AccountForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            @PageableDefault(size = 20) Pageable pageable) {

        // 新規登録時のID重複チェック
        if (form.isNew() && accountService.findAccountByUsername(form.getUsername()) != null) {
            result.rejectValue("username", "error.username", "指定されたユーザーIDは既に使用されています。");
        }

        if (result.hasErrors()) {
            log.error("アカウント保存エラー");
            for (ObjectError error : result.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }
            // エラー時は一覧画面（モーダル）を再表示
            Page<LoginUser> users = accountService.findWithPaging(pageable);
            model.addAttribute("users", users);
            model.addAttribute("userGroups", this.userGroupService.findAll());
            model.addAttribute("showModal", true);

            // 編集モードの場合、最後の管理者フラグを再設定
            if (!form.isNew()) {
                try {
                    model.addAttribute("isLastAdmin", accountService.isLastAdmin(form.getUsername()));
                } catch (Exception e) {
                    // ユーザーが存在しない場合などは無視
                }
            }
            return "admin/account/list";
        }

        try {
            if (accountService.saveAccount(form.toModel(), form.getPassword(), form.isNew())) {
                redirectAttributes.addFlashAttribute(MESSAGE, "アカウントを保存しました。");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "保存に失敗しました。（有効な管理者が0人になる操作はできません）");
            }

        } catch (Exception e) {
            log.error("アカウント保存エラー", e);
            redirectAttributes.addFlashAttribute("errorMessage", "システムエラーが発生しました。");
        }

        // TODO 削除
        form.getGroupIds().stream().forEach(System.out::println);

        return REDIRECT_TO_LIST;
    }

    /**
     * アカウントを削除します
     */
    @GetMapping("/delete")
    public String delete(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        try {
            if (accountService.deleteAccountByUsername(username)) {
                redirectAttributes.addFlashAttribute(MESSAGE, "アカウントを削除しました。");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "削除に失敗しました。（最後の管理者は削除できません）");
            }
        } catch (Exception e) {
            log.error("アカウント削除エラー", e);
            redirectAttributes.addFlashAttribute("errorMessage", "削除中にエラーが発生しました。");
        }
        return REDIRECT_TO_LIST;
    }
}