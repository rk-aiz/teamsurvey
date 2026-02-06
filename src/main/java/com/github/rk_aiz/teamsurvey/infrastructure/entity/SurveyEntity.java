package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
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
    /** アンケートの状態 */
    @Builder.Default
    private SurveyStatus status = SurveyStatus.DRAFT;
    /** 集計結果の公開範囲 */
    @Builder.Default
    private ResultVisibility resultVisibility = ResultVisibility.ADMIN_ONLY;
    /** 回答締め切り日時 (nullの場合は無期限) */
    private LocalDateTime deadline;
    /** アンケート作成日時 */
    private LocalDateTime createdAt;
    /** アンケート更新日時 */
    private LocalDateTime updatedAt;

    /**
     * Entity -> Domain Model 変換
     */
    public Survey toModel() {
    	Survey survey = new Survey();
    	BeanUtils.copyProperties(this, survey);
    	
        return survey;
    }

    /**
     * Domain Model -> Entity 変換
     */
    public static SurveyEntity fromModel(Survey model) {
        SurveyEntity entity = new SurveyEntity();
        BeanUtils.copyProperties(model, entity);

        return entity;
    }
}
