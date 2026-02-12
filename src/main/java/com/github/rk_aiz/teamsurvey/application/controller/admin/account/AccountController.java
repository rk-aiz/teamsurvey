package com.github.rk_aiz.teamsurvey.application.controller.admin.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.form.AccountForm;
import com.github.rk_aiz.teamsurvey.application.form.UserGroupForm;
import com.github.rk_aiz.teamsurvey.application.mapper.AccountFormMapper;
import com.github.rk_aiz.teamsurvey.domain.exception.ServiceRuleException;
import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;
import com.github.rk_aiz.teamsurvey.domain.service.UserGroupService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Responseの一覧/詳細画面のコントローラー
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/account")
public class AccountController {

    /** 定数 */
    private static final String MESSAGE = "message";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String REDIRECT_TO_LIST = "redirect:/admin/account/list";

    /** DI */
    private final AccountService accountService;
    private final AccountFormMapper accountFormMapper;
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
        Page<UserAccount> users = this.accountService.findWithPaging(pageable);
        model.addAttribute("users", users);
        model.addAttribute("userGroups", this.userGroupService.findAll());
        model.addAttribute("userGroupForm", new UserGroupForm());

        // モーダル表示用の処理
        if ("new".equals(action)) {
            // 新規作成
            // Recordのため全引数コンストラクタを使用 (nullは許容)
            AccountForm form = new AccountForm(null, null, null, null, null, false, null, new ArrayList<>(), true);
            model.addAttribute("accountForm", form);
            model.addAttribute("showModal", true);
        } else if ("edit".equals(action) && username != null) {
            // 編集
            try {
                model.addAttribute("accountForm",
                        accountFormMapper.toForm(this.accountService.findAccountByUsername(username), false));
                model.addAttribute("isLastAdmin", accountService.isLastAdmin(username));
                model.addAttribute("showModal", true);
            } catch (IllegalArgumentException | NoSuchElementException e) {
                log.warn("指定されたユーザーが見つかりません: {}", username);
            }
        }

        return "admin/account/list";
    }

    /**
     * アカウント情報を保存(新規登録・更新)します
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<Map<String, String>> save(
            @Validated @RequestBody AccountForm accountForm,
            BindingResult result) {

        if (result.hasErrors()) {
            // エラーメッセージのMapを作成して 400 Bad Request で返す
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            boolean saveSuccess = accountService.saveAccount(
                    accountFormMapper.toModel(accountForm),
                    accountForm.password(),
                    accountForm.isNew());
            if (saveSuccess) {
                return ResponseEntity.ok(Map.of(MESSAGE, "アカウントを保存しました。"));
            } else {
                return ResponseEntity.internalServerError().body(Map.of(ERROR_MESSAGE, "保存に失敗しました。"));
            }
        } catch (ServiceRuleException e) {
            // ビジネスルール違反(重複など)はクライアントエラー(400)として返す
            return ResponseEntity.badRequest().body(Map.of(ERROR_MESSAGE, e.getMessage()));
        } catch (Exception e) {
            log.error("アカウント保存エラー", e);
            return ResponseEntity.internalServerError().body(Map.of(ERROR_MESSAGE, "システムエラーが発生しました。"));
        }
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
                redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "削除に失敗しました。(最後の管理者は削除できません)");
            }
        } catch (Exception e) {
            log.error("アカウント削除エラー", e);
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "削除中にエラーが発生しました。");
        }
        return REDIRECT_TO_LIST;
    }
}