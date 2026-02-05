package com.github.rk_aiz.teamsurvey.application.advice;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class LayoutAdvice {

    /**
     * 全てのビューに対して、権限に応じたレイアウトテンプレート名を "layout" 属性として提供します。
     */
    @ModelAttribute("layout")
    public String layout(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return "admin/admin_layout";
        }
        // デフォルト（未ログイン含む）はユーザーレイアウト
        return "user/user_layout";
    }
}