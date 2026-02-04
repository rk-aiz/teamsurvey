package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
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
    /** 回答パターン（結合用） */
    private String email;
    /** 質問詳細 */
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
    public LoginUser toModel() {
        LoginUser user = new LoginUser(
                this.username,
                this.password,
                this.createdAt,
                this.updatedAt,
                this.enabled,
                new ArrayList<>()
        );
        user.setEmail(this.email);
        user.setDisplayName(this.displayName);
        return user;
    }

    /**
     * Domain Model -> Entity 変換
     */
    public static AuthenticationEntity from(LoginUser model) {
        AuthenticationEntity entity = new AuthenticationEntity();
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}
