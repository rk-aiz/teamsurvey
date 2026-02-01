package com.github.rk_aiz.teamsurvey.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.type.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    /** 回答ID */
    private Integer responseId;
    /** アンケートID */
    private Integer surveyId;
    /** 回答ユーザー名 */
    private String username;
    /** 回答ステータス */
    @Builder.Default
    private ResponseStatus status = ResponseStatus.UNVERIFIED;
    /** 回答日時 */
    private LocalDateTime createdAt;
    /** 更新日時 (再回答時) */
    private LocalDateTime updatedAt;

    /** 質問に対する回答のリスト */
    private List<ResponseDetail> responseDetails;
}
