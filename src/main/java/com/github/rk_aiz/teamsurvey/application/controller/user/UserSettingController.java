package com.github.rk_aiz.teamsurvey.application.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.form.AccountForm;
import com.github.rk_aiz.teamsurvey.application.mapper.AccountFormMapper;
import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/setting")
@RequiredArgsConstructor
public class UserSettingController {

    private static final String MESSAGE = "message";
    private static final String ERROR_MESSAGE = "errorMessage";

    private final AccountService accountService;
    private final AccountFormMapper accountMapper;

    @GetMapping
    public String showSetting(
            @AuthenticationPrincipal LoginUser loginUser,
            Model model,
            RedirectAttributes redirectAttributes) {

        // DBから最新のユーザー情報を取得
        UserAccount latestAccount = accountService.findAccountByUsername(loginUser.getUsername());
        model.addAttribute(
                "accountForm",
                accountMapper.toForm(latestAccount, false));

        return "user/setting";
    }

    @PostMapping("/save")
    public String saveSetting(
            @AuthenticationPrincipal LoginUser loginUser,
            @Validated @ModelAttribute AccountForm accountForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "user/setting";
        }

        // Serviceの専用メソッドを呼び出す
        boolean success = accountService.updateProfile(
                loginUser.getUsername(),
                accountForm.displayName(),
                accountForm.email(),
                accountForm.password());

        if (success) {
            redirectAttributes.addFlashAttribute(MESSAGE, "アカウント設定を更新しました。");
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "更新に失敗しました。");
        }

        return "redirect:/setting";
    }
}
