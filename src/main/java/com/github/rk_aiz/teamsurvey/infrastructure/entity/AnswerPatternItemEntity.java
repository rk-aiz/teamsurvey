package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import lombok.Data;

/**
 * answer_pattern_itemsテーブルに対応するEntity
 */
@Data
public class AnswerPatternItemEntity {
    private Integer id;
    private Integer answerPatternId;
    private String itemText;
    private Integer itemOrder;
}
