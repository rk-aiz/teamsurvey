package com.github.rk_aiz.teamsurvey.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SurveyStatus {
    /** 下書き（編集可能・回答不可） */
    DRAFT(0, "下書き"),
    /** 公開中（編集不可・回答可能） */
    PUBLISHED(10, "公開中"),
    /** 公開停止（回答不可・再開可能） */
    SUSPENDED(20, "公開停止"),
    /** 終了（編集不可・回答不可） */
    CLOSED(30, "終了"),
    /** 削除済み */
    DELETED(90, "削除済み");

    private final int step;
    private final String label;

    /**
     * 指定されたステータス以降（進行度）かどうかを判定します。
     */
    public boolean isAtLeast(SurveyStatus other) {
        return this.step >= other.step;
    }

    public boolean canChangeToClose() {
        return 10 <= this.step && this.step <= 20;
    }
}