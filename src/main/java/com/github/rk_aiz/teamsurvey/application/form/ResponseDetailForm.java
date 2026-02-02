package com.github.rk_aiz.teamsurvey.application.form;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.ResponseDetail;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;

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
        form.setQuestionId(question.getQuestionId());
        form.setCheckboxOptionIds(new ArrayList<>());
        return form;
    }

    public ResponseDetail toModel(Question question) {
        ResponseDetail detail = new ResponseDetail();
        BeanUtils.copyProperties(this, detail);
        switch(question.getType()) {
            case TEXT -> detail.setRawData(this.getResponseText());
            case RADIO -> detail.setRawData(this.getRadioOptionId().toString());
            case CHECKBOX -> detail.setRawData(this.getCheckboxOptionIds().toString()); // [1, 2, 3] のような形式で保存される
        }
        
        return detail;
    }

}