package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.util.List;

import lombok.Data;

@Data
public class AnswerPatternEntity {
    private Integer id;
    private String patternName;

    // 1つのパターンは複数の選択肢項目を持つ
    private List<AnswerPatternItemEntity> items;

    /**
     * この回答パターンが空（未設定）かどうかを判定します。
     */
    public boolean isEmpty() {
        return this.id == null;
    }
}
