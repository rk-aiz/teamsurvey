package com.github.rk_aiz.teamsurvey.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SurveyStatus {
    /** 下書き（編集可能・回答不可） */
    DRAFT("下書き"),
    /** 公開中（編集注意・回答可能） */
    PUBLISHED("公開中"),
    /** 公開停止（回答不可・再開可能） */
    SUSPENDED("公開停止"),
    /** 終了（編集不可・回答不可） */
    CLOSED("終了"),
    /** 削除済み */
    DELETED("削除済み");

    private final String label;
}