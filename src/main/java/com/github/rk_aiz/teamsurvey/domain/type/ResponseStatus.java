package com.github.rk_aiz.teamsurvey.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    /** 未検証(初期状態) */
    UNVERIFIED("未検証"),
    /** 有効(集計対象) */
    VALID("有効"),
    /** 重複エラー(同一ユーザー/IPからの多重回答など) */
    DUPLICATE("重複エラー"),
    /** 不正形式(バリデーションエラーや改ざん検知) */
    INVALID("不正形式"),
    /** テストデータ(管理者によるテスト回答) */
    TEST("テストデータ");

    private final String label;
}