package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.util.ArrayList;
import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.ResponseDetail;
import com.github.rk_aiz.teamsurvey.domain.model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDetailEntity {
    /** 主キー */
    private Integer id;
    /** 回答ヘッダーID */
    private Integer responseId;
    /** 設問ID */
    private Integer questionId;
    /** 選択肢ID (選択式の場合) */
    private Integer answerPatternItemId;
    /** 回答テキスト (自由記述の場合) */
    private String answerText;

    public static List<ResponseDetailEntity> from(
    		Response response, ResponseDetail detail) {
        List<ResponseDetailEntity> entities = new ArrayList<>();
        switch (detail.getQuestion().getType()) {
            case TEXT -> {
                entities.add(
                    new ResponseDetailEntity(
                        detail.getResponseDetailId(),
                        response.getResponseId(),
                        detail.getQuestion().getQuestionId(),
                        null,
                        detail.getRawData()
                    ));
            }
            case RADIO -> {
                entities.add(
                    new ResponseDetailEntity(
                        detail.getResponseDetailId(),
                        response.getResponseId(),
                        detail.getQuestion().getQuestionId(),
                        detail.getSingleChoiceResponse(),
                        null
                    )
                );
            }
            case CHECKBOX -> {
                for (Integer optionId : detail.getMultiChoiceResponses()) {
                    entities.add(
                        new ResponseDetailEntity(
                            detail.getResponseDetailId(),
                            response.getResponseId(),
                            detail.getQuestion().getQuestionId(),
                            optionId,
                            null
                        )
                    );
                }
            }
        }
        return entities;
    }
}
