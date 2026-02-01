package com.github.rk_aiz.teamsurvey.application.validation;

import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;

import jakarta.validation.groups.Default;

/*
 * Validationグループ用の空のインターフェース
 * SurveyがDraft時のValidationグループ
 */
public interface SurveyValidationGroup extends Default {

    public static Class<? extends SurveyValidationGroup> getValidationGroup(
            SurveyStatus status) {
        return switch (status) {
            case DRAFT -> OnDraftSurvey.class;
            case PUBLISHED -> OnPublishedSurvey.class;
            case SUSPENDED -> OnSuspendedSurvey.class;
            case CLOSED -> OnClosedSurvey.class;
            case DELETED -> OnDeletedSurvey.class;
            default -> SurveyValidationGroup.class;
        };
    }
}
