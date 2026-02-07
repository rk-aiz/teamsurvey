package com.github.rk_aiz.teamsurvey.application.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountForm {

    /** ユーザー名 */
    @Size(max = 50, message = "ユーザー名は{max}文字以内で入力してください")
    @NotBlank(message = "ユーザー名は必須です")
    private String username;

    /** パスワード */
    @Size(max = 255, message = "パスワードは{max}文字以内で入力してください")
    private String password;

    /** パスワード（確認） */
    private String passwordConfirmation;

    /** 表示名 */
    @Size(max = 50, message = "表示名は{max}文字以内で入力してください")
    @NotBlank(message = "表示名は必須です")
    private String displayName;

    /** メールアドレス */
    @Size(max = 255, message = "メールアドレスは{max}文字以内で入力してください")
    @NotBlank(message = "メールアドレスは必須です")
    private String email;

    /** 有効フラグ */
    private boolean enabled;

    private LocalDateTime createdAt;

    private List<Integer> groupIds = new ArrayList<>();

    private boolean isNew;

    /**
     * Model -> Form
     */
    public static AccountForm from(LoginUser model, boolean isNew) {
        AccountForm form = new AccountForm(
                model.getUsername(),
                null,
                null,
                model.getDisplayName(),
                model.getEmail(),
                model.isEnabled(),
                model.getCreatedAt(),
                model.getAssignedGroups().stream().map(UserGroup::getGroupId).toList(),
                isNew);

        // ハッシュ化されたパスワードを誤ってThymeleafで使用しないようにクリア
        form.setPassword(null);
        return form;
    }

    /*
     * Form -> Model
     */
    public LoginUser toModel() {
        LoginUser loginUser = new LoginUser(
                getUsername(),
                "", // パスワードはService層でハッシュ化・設定するため、ここではダミー(空文字)を渡す
                isNew ? LocalDateTime.now() : getCreatedAt(),
                null, // 更新日時は実際にDBに保存されたときにDBで設定する為、ここでは設定しない
                isEnabled(),
                null); // 権限は、直接サービスで更新する為、Formからは取得しない

        loginUser.setDisplayName(displayName);
        loginUser.setEmail(email);

        return loginUser;
    }

    /**
     * パスワードの必須チェック（新規登録時のみ必須）
     */
    @AssertTrue(message = "パスワードは必須です")
    public boolean isPasswordRequired() {
        if (isNew && (password == null || password.isBlank())) {
            return false;
        }
        return true;
    }

    /**
     * パスワードの一致チェック
     */
    @AssertTrue(message = "パスワード（確認）が一致しません")
    public boolean isPasswordMatching() {
        if (password == null || password.isBlank())
            return true;
        return password.equals(passwordConfirmation);
    }
}