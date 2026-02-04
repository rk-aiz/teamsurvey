package com.github.rk_aiz.teamsurvey.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;

public interface AccountService {

    /** 全てのアカウントを取得します */
    Page<LoginUser> findWithPaging(Pageable pageable);

    /**
     * アカウント詳細を取得します。
     * @param username アカウントID
     * @throws IllegalArgumentException アカウントが存在しない場合
     */
    LoginUser findAccountByUsername(String username) throws IllegalArgumentException;

    LoginUser saveAccount(LoginUser loginUser);

    boolean deleteAccountByUsername(String username);
}