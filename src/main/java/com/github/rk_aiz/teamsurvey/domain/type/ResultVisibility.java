package com.github.rk_aiz.teamsurvey.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultVisibility {
    /** 管理者のみ閲覧可能 */
    ADMIN_ONLY("管理者のみ"),
    /** 回答対象グループ（survey_target_groups）に属するユーザーのみ */
    TARGET_GROUP("対象グループのみ"),
    /** ログインしている全ユーザー */
    ALL_USER("ログインユーザー全体");

    private final String label;
}