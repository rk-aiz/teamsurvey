package com.github.rk_aiz.teamsurvey.domain.model.result;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.type.QuestionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 設問ごとの集計結果を表すクラス
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAggregation {

    private Integer questionId;
    /** 設問情報 */
    private Question question;

    /** この設問への有効回答数 */
    private int answerCount;

    /**
     * 選択式回答の集計結果リスト
     * (ラジオボタン、チェックボックス用)
     */
    private List<OptionStat> optionStats;

    /**
     * 自由記述回答のリスト
     * (テキストボックス用)
     */
    private List<String> textAnswers;

    /**
     * 選択式の設問かどうかを判定(Viewでの分岐用)
     */
    public boolean isSelectionType() {
        return question.getType() == QuestionType.RADIO || question.getType() == QuestionType.CHECKBOX;
    }

    /**
     * 選択肢ごとの統計情報
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionStat {
        /** 選択肢ID */
        private Integer optionId;
        /** 選択肢名 */
        private String label;
        /** 選択数 */
        private int count;
        /** 割合 (%) */
        private double percentage;
    }
}