package com.github.rk_aiz.teamsurvey.domain.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

/**
 * ユーザー情報を表すドメインモデル
 */
@Getter
public class LoginUser extends User {

    private String displayName;

    /**
     * コンストラクタ
     * 最低限の情報を保持したUserDetails
     * 実装クラスUserを作成する
     */
    public LoginUser(
            String username,
            String password,
            String displayName,
            boolean enabled,
            Collection<? extends GrantedAuthority> authorities) {
        super(
                username,
                password,
                enabled,
                true,
                true,
                true, authorities);
        this.displayName = displayName;
    }
}