package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import org.springframework.beans.BeanUtils;
import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * answer_patternsテーブルに対応するEntity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerPatternEntity {
    private Integer id;
    private String patternName;
    private boolean isDeleted;

    /**
     * Entity -> Domain Model 変換
     */
    public AnswerOption toModel() {
        AnswerOption model = new AnswerOption();
        BeanUtils.copyProperties(this, model);
        model.setAnswerOptionId(this.id);
        model.setName(patternName);
        return model;
    }
    /**
     * Domain Model -> Entity 変換
     */
    public static AnswerPatternEntity from(AnswerOption model) {
        AnswerPatternEntity entity = new AnswerPatternEntity();
        BeanUtils.copyProperties(model, entity);
        entity.setId(model.getAnswerOptionId());
        entity.setPatternName(model.getName());
        return entity;
    }
}
