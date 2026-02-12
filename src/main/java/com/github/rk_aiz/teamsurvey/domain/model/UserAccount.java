package com.github.rk_aiz.teamsurvey.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.type.Authority;

/**
 * ユーザーアカウント
 */
public record UserAccount(
        String username,
        String password,
        String email,
        String displayName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean enabled,
        List<UserGroup> assignedGroups) {

    public boolean hasAutority(Authority authority) {
        if (this.assignedGroups == null)
            return false;

        return this.assignedGroups().stream()
                .anyMatch(group -> group.getAuthority() == authority);
    }

    public static UserAccount empty() {
        return new UserAccount(
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                null);
    }
}