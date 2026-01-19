package com.github.rk_aiz.teamsurvey.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerOption {

    private Integer answerOptionId;
    private String patternName;

    // 1つのパターンは複数の選択肢項目を持つ
    @Builder.Default
    private List<OptionItem> items = new ArrayList<>();

    /**
     * この回答パターンが空（未設定）かどうかを判定します。
     */
    public boolean isEmpty() {
        return this instanceof EmptyAnswerOption;
    }

    /** 未設定時の回答パターン */
    public static final AnswerOption EMPTY = new EmptyAnswerOption();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionItem {
        private Integer id;
        private Integer answerPatternId;
        private String itemText;
        private Integer itemOrder;
    }

    /**
     * 不変の空オブジェクトを表す内部クラス (Null Object Pattern)
     * セッターを無効化し、リストも空の不変リストを返します。
     */
    private static class EmptyAnswerOption extends AnswerOption {
        @Override
        public void setAnswerOptionId(Integer id) {
            throw new UnsupportedOperationException("AnswerOption.EMPTY is immutable.");
        }

        @Override
        public void setPatternName(String patternName) {
            throw new UnsupportedOperationException("AnswerOption.EMPTY is immutable.");
        }

        @Override
        public void setItems(List<OptionItem> items) {
            throw new UnsupportedOperationException("AnswerOption.EMPTY is immutable.");
        }

        @Override
        public List<OptionItem> getItems() {
            return Collections.emptyList();
        }
    }
}
