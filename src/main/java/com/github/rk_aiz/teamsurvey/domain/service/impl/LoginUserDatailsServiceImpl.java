package com.github.rk_aiz.teamsurvey.domain.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginUserDatailsServiceImpl implements UserDetailsService {

    /** DI */
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        LoginUser loginUser = accountRepository.findByUsername(username);

        if (loginUser == null) {
            throw new UsernameNotFoundException(
                    username + " => 指定しているユーザー名は存在しません");
        }

        // 対象データがあれば、UserDetailsの実装クラスを返す
        return loginUser;
    }
}
