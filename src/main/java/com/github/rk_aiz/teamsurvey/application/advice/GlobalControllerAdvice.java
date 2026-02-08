package com.github.rk_aiz.teamsurvey.application.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rk_aiz.teamsurvey.application.exception.LoginRequiredException;
import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;

@ControllerAdvice
public class GlobalControllerAdvice {

    // application.properties から app.name を取得（未設定時のデフォルトは "TeamSurvey"）
    @Value("${app.name:TeamSurvey}")
    private String appName;

    @ModelAttribute("appName")
    public String addAppNameToModel() {
        return appName;
    }

    /**
     * ログイン済みの場合、UserDetailsを "account" という名前でModelに追加します。
     */
    @ModelAttribute("account")
    public LoginUser addAccountToModel(@AuthenticationPrincipal LoginUser loginUser) {
        return loginUser;
    }

    /**
     * ログインが必要な場合の例外ハンドリング
     */
    @ExceptionHandler(LoginRequiredException.class)
    public String handleLoginRequiredException(LoginRequiredException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}