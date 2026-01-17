package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.time.LocalDateTime;

import com.github.rk_aiz.teamsurvey.domain.type.ResultVisibility;
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
public class Survey {
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

    /** ゲスト回答（未ログイン）を許可するか */
    private boolean allowGuest;

    /** 集計結果の公開範囲 */
    @Builder.Default
    private ResultVisibility resultVisibility = ResultVisibility.ADMIN_ONLY;

    /** 回答締め切り日時 (nullの場合は無期限) */
    private LocalDateTime deadline;
}
