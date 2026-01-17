package com.github.rk_aiz.teamsurvey.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionType {
    RADIO("単一選択肢"),
    CHECKBOX("複数選択可"),
    TEXT("テキスト入力");

    private final String label;
}