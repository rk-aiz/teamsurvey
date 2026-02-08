package com.github.rk_aiz.teamsurvey.application.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * AccountFormの相関チェック用アノテーション
 */
@Target({ ElementType.TYPE }) // クラスに対して付与する
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountFormValidator.class)
public @interface AccountFormCheck {
    String message() default "入力内容に誤りがあります";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}