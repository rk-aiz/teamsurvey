package com.github.rk_aiz.teamsurvey.domain.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.github.rk_aiz.teamsurvey.domain.type.Authority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup {
    private Integer id;
    private String groupName;
    private Authority authority;
    private boolean isSystemGroup;

    /**
     * このグループに割り当てられた権限リストを取得します。
     * <p>
     * ADMIN権限を持つ場合は、下位のUSER権限も自動的に付与されます。
     * </p>
     * 
     * @return Spring Securityで使用する権限オブジェクトのリスト
     */
    public List<GrantedAuthority> getAuthorityList() {
        return Optional.ofNullable(this.authority)
                .stream()
                .flatMap(auth -> auth == Authority.ADMIN
                        ? Stream.of(auth, Authority.USER)
                        : Stream.of(auth))
                .map(auth -> new SimpleGrantedAuthority(auth.name()))
                .map(GrantedAuthority.class::cast)
                .toList();
    }
}