package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginUserDatailsServiceImpl implements UserDetailsService {

    /** DI */
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAccount loginUser = accountRepository.findByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(
                    username + " => 指定しているユーザー名は存在しません");
        });

        Collection<? extends GrantedAuthority> authorities = loginUser
                .assignedGroups()
                .stream()
                .flatMap(group -> group.getAuthorityList().stream())
                .distinct()
                .toList();

        // 対象データがあれば、UserDetailsの実装クラスを返す
        return new LoginUser(
                loginUser.username(),
                loginUser.password(),
                loginUser.displayName(),
                loginUser.enabled(),
                authorities);
    }
}
