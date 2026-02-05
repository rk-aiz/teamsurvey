package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;

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

    public AnswerOption.OptionItem toModel() {
        AnswerOption.OptionItem model = new AnswerOption.OptionItem();
        model.setItemId(this.id);
        model.setItemText(this.itemText);
        model.setItemOrder(this.itemOrder);
        return model;
    }

    public static AnswerPatternItemEntity from(AnswerOption.OptionItem model) {
        AnswerPatternItemEntity entity = new AnswerPatternItemEntity();
        entity.setId(model.getItemId());
        entity.setItemText(model.getItemText());
        entity.setItemOrder(model.getItemOrder());
        return entity;
    }
}
