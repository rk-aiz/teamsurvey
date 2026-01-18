package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEntity {
    /** 主キー */
    private Integer id;
    /** アンケートID */
    private Integer surveyId;
    /** 回答ユーザーID (ゲストの場合はNULL) */
    private String userId;
    /** 回答日時 */
    private LocalDateTime createdAt;
    /** 更新日時 (再回答時) */
    private LocalDateTime updatedAt;
}
