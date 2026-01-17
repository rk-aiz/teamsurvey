package com.github.rk_aiz.teamsurvey.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Authority {
    ADMIN("管理者"),
    USER("ユーザー");

    private final String label;
}