package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.util.List;

import lombok.Data;

/**
 * answer_patternsテーブルに対応するEntity
 */
@Data
public class AnswerPatternEntity {
    private Integer id;
    private String patternName;
    private List<AnswerPatternItemEntity> items;
}
