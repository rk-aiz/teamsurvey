package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.application.mapper.AccountFormMapper;
import com.github.rk_aiz.teamsurvey.domain.exception.ServiceRuleException;
import com.github.rk_aiz.teamsurvey.domain.exception.SystemCriticalException;
import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;
import com.github.rk_aiz.teamsurvey.domain.service.UserGroupService;
import com.github.rk_aiz.teamsurvey.domain.type.Authority;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.AccountRepository;
import com.github.rk_aiz.teamsurvey.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    /** DI */
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserGroupService userGroupService;

    @Override
    public Page<UserAccount> findWithPaging(Pageable pageable) {
        // 総件数の取得
        long total = accountRepository.count();
        List<UserAccount> users;

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
        UserAccount user = accountRepository.findByUsername(username);

        // 2. ユーザーが存在しない、または既に無効化されている場合は false
        if (user == null || !user.enabled()) {
            return false;
        }

        // 3. そのユーザーが管理者権限を持っているか確認
        if (!user.hasAutority(Authority.ADMIN)) {
            return false;
        }

        // 4. システム全体の「有効な管理者」の数をカウント
        long activeAdminCount = accountRepository.countEnabledAdmins();

        // 5. 自身が有効な管理者で、かつ総数が1人以下なら true
        return activeAdminCount <= 1;
    }

    @Override
    public UserAccount findAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    /**
     * アカウント情報を保存します。
     * (ビジネスルールの検証処理を行い、saveAccountCoreに検証済みアカウント情報を渡します。)
     */
    @Override
    public boolean saveAccount(
            UserAccount accountToSave,
            String rawPassword,
            boolean isNew) {

        if (isNew && !StringUtils.hasText(rawPassword)) {
            throw new ServiceRuleException("新規登録時はパスワードが必須です。");
        }

        if (isNew && this.accountRepository.exists(accountToSave.username())) {
            throw new ServiceRuleException("指定されたユーザー名は既に使用されています。");
        }

        // 更新時のポリシー検証
        if (!isNew) {
            this.validateUpdatePolicy(accountToSave.username());
        }

        // 無効化ポリシーの検証
        if (!accountToSave.enabled()) {
            this.validateDisablePolicy(accountToSave.username());
        }

        // パスワードポリシーの検証(変更がある場合のみ)
        if (StringUtils.hasText(rawPassword)) {
            this.validatePasswordPolicy(rawPassword);
        }

        return saveAccountCore(
                accountToSave.username(),
                rawPassword,
                accountToSave.email(),
                accountToSave.displayName(),
                accountToSave.enabled(),
                accountToSave.assignedGroups());
    }

    /**
     * ユーザーアカウントを更新します。
     * 一般ユーザーが自身のアカウント情報を更新する為に使用します。
     */
    @Override
    public boolean updateProfile(String username, String displayName, String email, String rawPassword) {

        // 更新時のポリシー検証
        this.validateUpdatePolicy(username);

        // パスワードポリシーの検証(変更がある場合のみ)
        if (StringUtils.hasText(rawPassword)) {
            this.validatePasswordPolicy(rawPassword);
        }

        // 権限や有効フラグは既存のまま維持して更新
        return saveAccountCore(
                username,
                rawPassword,
                email,
                displayName,
                true,
                this.accountRepository.findByUsername(username).assignedGroups());
    }

    /**
     * ユーザーアカウント保存のコア処理
     */
    private boolean saveAccountCore(
            String username,
            String rawPassword,
            String email,
            String displayName,
            boolean isEnabled,
            List<UserGroup> userGroups) {

        Optional<UserAccount> currentAccount = Optional
                .ofNullable(this.accountRepository.findByUsername(username));

        String encodedPassword = Optional.ofNullable(rawPassword)
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .or(() -> currentAccount.map(UserAccount::password))
                .orElseThrow(() -> new SystemCriticalException("アカウント保存処理において、有効なパスワードが設定されていません。"));

        // 新しいインスタンスを作成する
        UserAccount accountToSave = new UserAccount(
                username,
                encodedPassword,
                email,
                displayName,
                currentAccount.map(UserAccount::createdAt).orElse(LocalDateTime.now()),
                LocalDateTime.now(),
                isEnabled,
                userGroups);

        return accountRepository.save(accountToSave);
    }

    @Override
    public boolean deleteAccountByUsername(String username) {
        // ★最後の管理者の場合は削除させない
        if (isLastAdmin(username)) {
            throw new ServiceRuleException("管理者アカウントを全て削除することはできません");
        }

        return accountRepository.remove(username);
    }

    @Override
    public boolean existsAnyAccount() {
        return accountRepository.count() > 0;
    }

    /**
     * 更新時のポリシー検証
     */
    private void validateUpdatePolicy(String username) {

        if (!this.accountRepository.exists(username)) {
            throw new ServiceRuleException("更新対象のアカウントが存在しません。ページをリロードしてください。");
        }
    }

    private void validateDisablePolicy(String username) {

        // ★最後の管理者を無効化しようとした場合はブロック
        if (isLastAdmin(username)) {
            throw new ServiceRuleException("管理者アカウントを全て無効にすることはできません");
        }
    }

    /**
     * パスワードポリシーの検証
     */
    private void validatePasswordPolicy(String rawPassword) {

        if (rawPassword.length() < 8) {
            throw new ServiceRuleException("パスワードは8文字以上で設定してください。");
        }
    }

    @Override
    public void createInitialAdmin(String username, String password) {
        if (this.existsAnyAccount()) {
            throw new ServiceRuleException("初回管理者アカウントは既に作成されています。");
        }

        UserAccount adminAccount = new UserAccount(
                username,
                passwordEncoder.encode(password),
                null,
                username,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                List.of(userGroupService.getOrCreateSystemAdminGroup()));

        accountRepository.save(adminAccount);
    }

}
