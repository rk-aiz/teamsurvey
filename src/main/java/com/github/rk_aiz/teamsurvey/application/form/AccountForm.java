package com.github.rk_aiz.teamsurvey.application.form;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;

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
    @NotBlank(message = "パスワードは必須です")
	private String password;

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

    private List<UserGroup> assignedGroups;

    private boolean isNew;

    /**
     * Model -> Form
     */
    public static AccountForm from(LoginUser model, boolean isNew) {
        AccountForm form = new AccountForm();
        BeanUtils.copyProperties(model, form);
        form.setNew(isNew);

        return form;
    }

    /*
     * Form -> Model
     */
    public LoginUser toModel() {
        return new LoginUser(
            this.getUsername(),
            this.getPassword(),
            this.isNew ? LocalDateTime.now() : this.getCreatedAt(),
            null,
            this.isEnabled(),
            this.getAssignedGroups().stream().flatMap(group -> group.getAuthorityList().stream()).toList()
        );
    }
}