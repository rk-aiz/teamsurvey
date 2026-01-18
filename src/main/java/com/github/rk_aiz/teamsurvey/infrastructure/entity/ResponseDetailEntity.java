package com.github.rk_aiz.teamsurvey.infrastructure.entity;

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
}
