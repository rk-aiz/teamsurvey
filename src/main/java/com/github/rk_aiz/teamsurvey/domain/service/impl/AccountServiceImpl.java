package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;
import com.github.rk_aiz.teamsurvey.domain.type.Authority;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    /** DI */
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<LoginUser> findWithPaging(Pageable pageable) {
        // 総件数の取得
        long total = accountRepository.count();
        List<LoginUser> users;

        if (total > 0) {
            // ページング指定で取得
            users = accountRepository.findWithPaging(pageable.getOffset(), pageable.getPageSize());
        } else {
            users = Collections.emptyList();
        }

        return new PageImpl<>(users, pageable, total);
    }

    /**
     * 指定されたユーザーが「最後の有効な管理者」かどうかを判定します。
     */
    @Override
    public boolean isLastAdmin(String username) {
        // 1. ユーザー情報の取得
        LoginUser user = accountRepository.findByUsername(username);

        // 3. ユーザーが存在しない、または既に無効化されている場合は false
        if (user == null || !user.isEnabled()) {
            return false;
        }

        // 2. そのユーザーが管理者権限を持っているか確認
        if (!user.hasRole(Authority.ADMIN)) {
            return false;
        }

        // 3. システム全体の「有効な管理者」の数をカウント
        long activeAdminCount = accountRepository.countEnabledAdmins();

        // 4. 自身が有効な管理者で、かつ総数が1人以下なら true
        return activeAdminCount <= 1;
    }

    @Override
    public LoginUser findAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public boolean saveAccount(LoginUser loginUser) {

        // パスワードが空でないことを保証する
        Assert.hasText(loginUser.getPassword(), "Password must not be empty before saving.");

        // ユーザーの存在チェック
        if (accountRepository.exists(loginUser.getUsername())) {
            // ★最後の管理者を無効化しようとした場合はブロック
            if (!loginUser.isEnabled() && isLastAdmin(loginUser.getUsername())) {
                return false;
            }

            return accountRepository.set(loginUser);
        } else {
            return accountRepository.add(loginUser);
        }
    }

    @Override
    public boolean saveAccount(LoginUser inputUser, String rawPassword, boolean isNew) {
        String passwordToSave;
        LocalDateTime createdAtToSave;

        if (isNew) {
            // 新規登録
            if (rawPassword == null || rawPassword.isEmpty()) {
                return false;
            }
            passwordToSave = passwordEncoder.encode(rawPassword);
            createdAtToSave = LocalDateTime.now();
        } else {
            // 更新: 既存情報を取得してマージ
            LoginUser existingUser = accountRepository.findByUsername(inputUser.getUsername());
            if (existingUser == null) {
                return false;
            }

            // パスワード処理: 入力があればハッシュ化、なければ既存維持
            if (rawPassword != null && !rawPassword.isEmpty()) {
                passwordToSave = passwordEncoder.encode(rawPassword);
            } else {
                passwordToSave = existingUser.getPassword();
            }
            createdAtToSave = existingUser.getCreatedAt();
        }

        // 保存用インスタンスの生成
        LoginUser userToSave = new LoginUser(
                inputUser.getUsername(),
                passwordToSave,
                createdAtToSave,
                LocalDateTime.now(), // updatedAt
                inputUser.isEnabled(),
                inputUser.getAuthorities());

        userToSave.setDisplayName(inputUser.getDisplayName());
        userToSave.setEmail(inputUser.getEmail());
        userToSave.setAssignedGroups(inputUser.getAssignedGroups());

        // 既存の保存メソッド（最後の管理者チェックなどを含む）に委譲
        return saveAccount(userToSave);
    }

    @Override
    public boolean deleteAccountByUsername(String username) {
        // ★追加: 最後の管理者の場合は削除させない
        if (isLastAdmin(username)) {
            return false;
        }
        return accountRepository.remove(username);
    }

    @Override
    public boolean updateProfile(String username, String displayName, String email, String rawPassword) {
        // 常に最新の情報をDBから取得して更新対象とする（セキュリティ対策）
        LoginUser user = accountRepository.findByUsername(username);
        if (user == null) {
            return false;
        }

        // パスワードの決定（変更がある場合はハッシュ化、なければ既存のまま）
        String passwordToSave = user.getPassword();
        if (rawPassword != null && !rawPassword.isEmpty()) {
            passwordToSave = passwordEncoder.encode(rawPassword);
        }

        // 新しいインスタンスを作成する
        LoginUser newUser = new LoginUser(
                user.getUsername(),
                passwordToSave,
                user.getCreatedAt(),
                LocalDateTime.now(), // 更新日時を現在時刻に設定
                user.isEnabled(),
                user.getAuthorities());

        newUser.setDisplayName(displayName);
        newUser.setEmail(email);
        newUser.setAssignedGroups(user.getAssignedGroups());

        return this.saveAccount(newUser);
    }
}
