package com.github.rk_aiz.teamsurvey.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;

public interface AccountService {

    /** 全てのアカウントを取得します */
    Page<LoginUser> findWithPaging(Pageable pageable);

    public boolean isLastAdmin(String username);

    LoginUser findAccountByUsername(String username);

    boolean saveAccount(LoginUser loginUser);

    /**
     * アカウント情報を保存します。（パスワードのハッシュ化や既存情報のマージを行います）
     * 
     * @param rawPassword 入力された生のパスワード（変更がない場合はnullまたは空文字）
     */
    boolean saveAccount(LoginUser inputUser, String rawPassword, boolean isNew);

    boolean deleteAccountByUsername(String username);

    /**
     * ユーザー自身のプロフィール（表示名、メール、パスワード）を更新します。
     * パスワードは入力がある場合（null/空文字以外）のみ更新されます。
     */
    boolean updateProfile(String username, String displayName, String email, String rawPassword);
}