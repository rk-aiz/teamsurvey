package com.github.rk_aiz.teamsurvey.domain.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.type.QuestionType;
import com.github.rk_aiz.teamsurvey.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDetail {
    /** 設問に対する回答の詳細ID */
    private Integer responseDetailId;
    /** 回答ヘッダーID */
    private Integer responseId;
    /** 設問ID */
    private Question question;
    /** 回答内容 */
    private String rawData;

    public QuestionType getType() {
        return this.getQuestion().getType();
    }

    public void setTextResponse(String text) {
        this.setRawData(text);
    }

    public String getTextResponse() {
        return this.getRawData();
    }

    public void setSingleChoiceResponse(Integer itemId) {
        this.setRawData(itemId.toString());
    }

    /**
     * 単一選択の回答を数値(ID)として取得します。
     */
    public Integer getSingleChoiceResponse() {
        return parseId(getRawData());
    }

    public void setMultiChoiceResponses(List<Integer> itemIds) {
        this.setRawData(itemIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));
    }

    /**
     * 複数選択の回答をIDリストとして取得します。
     */
    public List<Integer> getMultiChoiceResponses() {
        if (!StringUtils.hasText(this.getRawData())) {
            return Collections.emptyList();
        }

        return Arrays.stream(this.getRawData().split(","))
                .map(this::parseId)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 文字列を数値(ID)に変換します。変換できない場合はnullを返します。
     */
    private Integer parseId(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
