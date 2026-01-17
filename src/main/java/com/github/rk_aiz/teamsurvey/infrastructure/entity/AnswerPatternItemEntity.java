package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import lombok.Data;

@Data
public class AnswerPatternItemEntity {
    private Integer id;
    private Integer answerPatternId;
    private String itemText;
    private Integer itemOrder;
}
