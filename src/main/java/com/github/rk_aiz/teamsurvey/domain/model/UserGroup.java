package com.github.rk_aiz.teamsurvey.domain.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.github.rk_aiz.teamsurvey.domain.type.Authority;

import lombok.Data;

@Data
public class UserGroup {
    private Integer id;
    private String groupName;
    private Authority authority;
    private boolean isSystemGroup;

    public List<GrantedAuthority> getAuthorityList() {
        // 権限リスト
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 列挙型からロールを取得
        authorities.add(new SimpleGrantedAuthority(this.getAuthority().name()));

        // ADMIN ロールの場合、USER権限も付与
        if (this.getAuthority() == Authority.ADMIN) {
            authorities.add(new SimpleGrantedAuthority(Authority.USER.name()));
        }

        return authorities;
    }
}