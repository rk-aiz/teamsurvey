package com.github.rk_aiz.teamsurvey.domain.model.question;

import com.github.rk_aiz.teamsurvey.domain.type.QuestionType;

import lombok.Builder;
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
public class SingleChoiceQuestion extends Question {

    @Builder.Default
    private AnswerOption answerOption = AnswerOption.EMPTY;

    @Override
    public QuestionType getType() {
        return QuestionType.RADIO;
    }

    @Override
    public boolean isValidQuestion() {
        return super.isValidQuestion() && !answerOption.isEmpty();
    }

    @Override
    public Question createCopy() {
        return SingleChoiceQuestion.builder()
                .text(this.getText())
                .required(this.isRequired())
                .answerOption(this.getAnswerOption())
                .build();
    }

    public void setAnswerOption(AnswerOption answerOption) {
        this.answerOption = (answerOption != null) ? answerOption : AnswerOption.EMPTY;
    }
}