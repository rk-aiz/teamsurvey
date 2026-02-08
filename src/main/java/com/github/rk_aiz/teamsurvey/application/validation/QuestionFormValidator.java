package com.github.rk_aiz.teamsurvey.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.github.rk_aiz.teamsurvey.application.form.QuestionForm;
import com.github.rk_aiz.teamsurvey.domain.type.QuestionType;

public class QuestionFormValidator implements ConstraintValidator<QuestionFormCheck, QuestionForm> {

    @Override
    public boolean isValid(QuestionForm value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        // typeがnullの場合は、@NotNullアノテーション側でエラーになるのでスルーする
        if (value.getType() == null) {
            return true;
        }

        // 自由記述(TEXT)以外で、回答パターンが空の場合はエラー
        if (value.getType() != QuestionType.TEXT && value.getAnswerOptionId() == null) {

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("回答パターンを設定してください")
                    .addPropertyNode("answerOptionId") // エラーを answerOptionId プロパティに紐づける
                    .addConstraintViolation();

            return false;
        }
        return true;
    }
}
