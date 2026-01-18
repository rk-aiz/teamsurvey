package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.time.LocalDateTime;

import com.github.rk_aiz.teamsurvey.domain.type.QuestionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity {
    /** 主キー */
    private Integer id;
    /** 対象アンケート */
    private Integer enqueteId;
    /** 質問詳細 */
    private String text;
    /** 表示順序 */
    private Integer displayOrder;
    /** 回答モード */
    @Builder.Default
    private QuestionType type = QuestionType.RADIO;
    /** 必須回答かどうか */
    private boolean required;
    /** 質問作成日時 */
    private LocalDateTime createdAt;
    /** 質問更新日時 */
    private LocalDateTime updatedAt;
    /** 回答パターン（結合用） */
    private AnswerPatternEntity answerPattern;
}
