package com.github.rk_aiz.teamsurvey.domain.model.question;

import com.github.rk_aiz.teamsurvey.domain.type.QuestionType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FreeResponseQuestion extends Question {

    @Override
    public QuestionType getType() {
        return QuestionType.TEXT;
    }

    @Override
    public Question createCopy() {
        return FreeResponseQuestion.builder()
                .text(this.getText())
                .required(this.isRequired())
                .build();
    }
}