package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;

public interface AccountService {

    /** 全てのアカウントを取得します */
    Page<UserAccount> findWithPaging(Pageable pageable);

    public boolean isLastAdmin(String username);

    Optional<UserAccount> findAccountByUsername(String username);

    boolean saveAccount(UserAccount inputUser, String rawPassword, boolean isNew);

    boolean deleteAccountByUsername(String username);

    /**
     * ユーザー自身のプロフィール(表示名、メール、パスワード)を更新します。
     * パスワードは入力がある場合(null/空文字以外)のみ更新されます。
     */
    boolean updateProfile(String username, String displayName, String email, String rawPassword);

    void createInitialAdmin(UserAccount userAccount);

    boolean existsAdmin();
}