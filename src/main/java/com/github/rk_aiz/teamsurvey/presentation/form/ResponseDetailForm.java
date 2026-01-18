package com.github.rk_aiz.teamsurvey.presentation.form;

import java.util.List;

import lombok.Data;

@Data
public class ResponseDetailForm {
    private Integer questionId;

    /** 自由記述の回答テキスト */
    private String responseText;
    /** 単一選択の回答 (Item ID) */
    private Integer radioOptionId;
    /** 複数選択の回答 (Item ID List) */
    private List<Integer> checkboxOptionIds;
}