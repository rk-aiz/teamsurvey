package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationEntity {
    /** 主キー */
    private String username;
    /** パスワード */
    private String password;
    /** メールアドレス */
    private String email;
    /** 表示名 */
    private String displayName;
    /** 作成日時 */
    private LocalDateTime createdAt;
    /** 更新日時 */
    private LocalDateTime updatedAt;
    /** 有効フラグ */
    private boolean enabled;

    private List<UserGroup> assignedGroups;

    /**
     * Entity -> Domain Model 変換
     */
    public UserAccount toModel() {
        return new UserAccount(
                this.username,
                this.password,
                this.email,
                this.displayName,
                this.createdAt,
                this.updatedAt,
                this.enabled,
                this.getAssignedGroups());
    }

    /**
     * Domain Model -> Entity 変換
     */
    public static AuthenticationEntity from(UserAccount model) {
        AuthenticationEntity entity = new AuthenticationEntity();
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}
