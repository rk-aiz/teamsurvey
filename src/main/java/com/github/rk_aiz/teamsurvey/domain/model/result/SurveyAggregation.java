package com.github.rk_aiz.teamsurvey.domain.model.result;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * アンケート全体の集計結果を表すクラス
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAggregation {

    /** 対象のアンケート情報 */
    private Survey survey;

    /** 総回答数 */
    private int totalResponseCount;

    /** 各設問の集計結果リスト */
    private List<QuestionAggregation> questionAggregations;
}