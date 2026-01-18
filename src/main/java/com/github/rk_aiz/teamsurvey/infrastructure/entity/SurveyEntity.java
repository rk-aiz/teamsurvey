package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.type.ResultVisibility;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * surveysテーブルに対応するEntity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyEntity {

    /** 主キー */
    private Integer id;
    /** アンケート名 */
    private String title;
    /** アンケート作成日時 */
    private LocalDateTime createdAt;
    /** アンケート更新日時 */
    private LocalDateTime updatedAt;
    /** アンケートの状態 */
    @Builder.Default
    private SurveyStatus status = SurveyStatus.DRAFT;
    /** 集計結果の公開範囲 */
    @Builder.Default
    private ResultVisibility resultVisibility = ResultVisibility.ADMIN_ONLY;
    /** 回答締め切り日時 (nullの場合は無期限) */
    private LocalDateTime deadline;
    /** 設問リスト */
    private List<Question> questions;

    public Survey toModel() {
        return Survey.builder()
                .surveyId(id)
                .title(title)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deadline(deadline)
                .status(status)
                .resultVisibility(resultVisibility)
                .questions(questions)
                .build();
    }
}
