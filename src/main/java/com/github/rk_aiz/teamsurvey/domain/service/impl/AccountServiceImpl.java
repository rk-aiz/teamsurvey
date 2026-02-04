package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.LoginUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    
    /** DI */
    private final LoginUserRepository loginUserRepository;

    @Override
    public Page<LoginUser> findWithPaging(Pageable pageable) {
        // 総件数の取得
        long total = loginUserRepository.count();
        List<LoginUser> users;

        if (total > 0) {
            // ページング指定で取得
            users = loginUserRepository.findWithPaging(pageable.getOffset(), pageable.getPageSize());
        } else {
            users = Collections.emptyList();
        }

        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public LoginUser findAccountByUsername(String username) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAccountByUsername'");
    }

    @Override
    public LoginUser saveAccount(LoginUser loginUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAccount'");
    }

    @Override
    public boolean deleteAccountByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAccountByUsername'");
    }
}
