package com.github.rk_aiz.teamsurvey.application.form;

import java.util.ArrayList;
import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.question.Question;

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

    public static ResponseDetailForm fromQuestion(Question question) {
        ResponseDetailForm form = new ResponseDetailForm();
        form.setQuestionId(question.getQuestionId());
        form.setCheckboxOptionIds(new ArrayList<>());
        return form;
    }
}