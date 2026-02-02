package com.github.rk_aiz.teamsurvey.domain.model;

import java.time.LocalDateTime;

import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * アンケートのヘッダー情報を表すクラス
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveySummary {

    /** 主キー */
    private Integer surveyId;

    /** アンケート名 */
    private String title;

    /** 回答締め切り日時 (nullの場合は無期限) */
    private LocalDateTime deadline;

    /** アンケートの状態 */
    @Builder.Default
    private SurveyStatus status = SurveyStatus.DRAFT;

    private Integer numberOfResponses;

    /**
     * 集計結果が公開期間に入っているか（締め切りを過ぎているか）を判定します。
     * 
     * @return true: 公開期間中, false: まだ公開期間ではない
     */
    public boolean isResultPublished() {
        return this.deadline != null && this.deadline.isBefore(LocalDateTime.now());
    }

    /**
     * 現在回答を受け付けている状態かを判定します。
     * 
     * @return true: 回答可能, false: 回答不可
     */
    public boolean isResponsible() {
        return this.status == SurveyStatus.PUBLISHED;
    }
}
