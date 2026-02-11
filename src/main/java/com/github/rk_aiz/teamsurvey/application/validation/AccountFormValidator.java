package com.github.rk_aiz.teamsurvey.application.validation;

import com.github.rk_aiz.teamsurvey.application.form.AccountForm;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountFormValidator implements ConstraintValidator<AccountFormCheck, AccountForm> {

    @Override
    public boolean isValid(AccountForm form, ConstraintValidatorContext context) {
        if (form == null) {
            return true;
        }

        boolean isValid = true;

        // 1. パスワード必須チェック (新規時のみ)
        if (form.isNew() && (form.password() == null || form.password().isBlank())) {
            context.disableDefaultConstraintViolation(); // デフォルトのエラー(クラスレベル)を無効化
            context.buildConstraintViolationWithTemplate("パスワードは必須です")
                    .addPropertyNode("password") // "password" フィールドにエラーを紐づける
                    .addConstraintViolation();
            isValid = false;
        }

        // 2. パスワード一致チェック
        if (form.password() != null && !form.password().isBlank()) {
            if (!form.password().equals(form.passwordConfirmation())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("パスワード(確認)が一致しません")
                        .addPropertyNode("passwordConfirmation") // "passwordConfirmation" フィールドにエラーを紐づける
                        .addConstraintViolation();
                isValid = false;
            }
        }

        return isValid;
    }
}