package com.github.rk_aiz.teamsurvey.application.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.github.rk_aiz.teamsurvey.application.validation.AccountFormCheck;

@AccountFormCheck // カスタムバリデーションを適用
public record AccountForm(

        /** ユーザー名 */
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "ユーザー名は英数字と記号(._-)のみ使用可能です") 
        @Size(max = 50, message = "ユーザー名は{max}文字以内で入力してください") 
        @NotBlank(message = "ユーザー名は必須です") 
        String username,

        /** パスワード */
        @Size(min = 8, max = 255, message = "パスワードは{min}文字以上、{max}文字以内で入力してください")
        String password,

        /** パスワード(確認) */
        String passwordConfirmation,
        /** 表示名 */
        @Size(max = 50, message = "表示名は{max}文字以内で入力してください") 
        @NotBlank(message = "表示名は必須です") 
        String displayName,

        /** メールアドレス */
        @Size(max = 255, message = "メールアドレスは{max}文字以内で入力してください")
        @NotBlank(message = "メールアドレスは必須です") 
        String email,

        /** 有効フラグ */
        Boolean enabled,

        LocalDateTime createdAt,

        List<Integer> groupIds,

        Boolean isNew) {

    /**
     * コンパクトコンストラクタ
     * 生成時にnullを正規化します
     */
    public AccountForm {
        enabled = enabled != null && enabled;

        // 空文字の場合はnullに変換して、@Sizeチェックをスキップさせる(更新時の「変更なし」に対応)
        password = (password != null && password.isEmpty()) ? null : password;
        passwordConfirmation = (passwordConfirmation != null && passwordConfirmation.isEmpty()) ? null
                : passwordConfirmation;
    }

    /**
     * 新規登録用の空のフォームを生成します。
     * @return 新規登録用の初期状態を持つAccountForm
     */
    public static AccountForm empty() {
        return new AccountForm(
            null, 
            null, 
            null, 
            null, 
            null, 
            true, 
            null, 
            new ArrayList<>(), 
            true
        );
    }

    /**
     * Thymeleaf等でプロパティとしてアクセスするためにGetterを追加
     */
    public boolean getIsNew() {
        return Optional.ofNullable(isNew).orElse(false);
    }

    public Boolean isNew() {
        return Optional.ofNullable(isNew).orElse(false);
    }
}