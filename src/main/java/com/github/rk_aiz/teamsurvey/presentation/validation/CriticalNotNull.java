package com.github.rk_aiz.teamsurvey.presentation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {}) // バリデータロジックは既存のものを使うので空でOK
@NotNull // 既存のチェックを流用
@ReportAsSingleViolation // 重要：内部のエラーを隠蔽し、このアノテーションのエラーとして報告する
public @interface CriticalNotNull {

    String message() default "不正なリクエストです";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}