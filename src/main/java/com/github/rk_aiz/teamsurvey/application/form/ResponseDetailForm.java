package com.github.rk_aiz.teamsurvey.application.form;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.ResponseDetail;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.type.QuestionType;
import com.github.rk_aiz.teamsurvey.util.StringUtils;

import lombok.Data;

@Data
public class ResponseDetailForm {

    /** 回答詳細ID */
    private Integer responseDetailId;
    /** 設問ID */
    private Integer questionId;
    /** 自由記述の回答テキスト */
    private String responseText;
    /** 単一選択の回答 (Item ID) */
    private Integer radioOptionId;
    /** 複数選択の回答 (Item ID List) */
    private List<Integer> checkboxOptionIds;

    public static ResponseDetailForm fromQuestion(Question question) {
        ResponseDetailForm form = new ResponseDetailForm();
        form.setQuestionId(question.getId());
        form.setCheckboxOptionIds(new ArrayList<>());
        return form;
    }

    public ResponseDetail toModel(Question question) {
        ResponseDetail detail = new ResponseDetail();
        BeanUtils.copyProperties(this, detail);
        detail.setQuestion(question);
        detail.setRawData(this.getRawData(question.getType()));

        return detail;
    }

    private String getRawData(QuestionType questionType) {
        return switch (questionType) {
            case TEXT -> StringUtils.trim(this.getResponseText());
            case RADIO -> this.getRadioOptionId().toString();
            case CHECKBOX -> this.getCheckboxOptionIds().stream().map(String::valueOf).collect(Collectors.joining(","));
        };
    }
}