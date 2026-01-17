package com.github.rk_aiz.teamsurvey.domain.model.question;

import java.time.LocalDateTime;

import com.github.rk_aiz.teamsurvey.domain.type.QuestionType;
import com.github.rk_aiz.teamsurvey.domain.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Question {

    /** 設問ID */
    private Integer questionId;
    /** 対象アンケート */
    private Integer surveyId;
    /** 質問詳細 */
    private String text;
    /** 表示順序 */
    private Integer displayOrder;
    /** 必須回答かどうか */
    private boolean required;
    /** 質問作成日時 */
    private LocalDateTime createdAt;
    /** 質問更新日時 */
    private LocalDateTime updatedAt;

    /**
     * 設問タイプを取得します。
     */
    public abstract QuestionType getType();

    /**
     * 設問として有効な状態かどうかを判定します。
     * (質問文が入力されているか、選択式の場合は回答パターンが設定されているか)
     */
    public boolean isValidQuestion() {
        return StringUtils.hasText(this.getText());
    }

    /**
     * 質問の複製を作成します。
     * 
     * @return 複製されたQuestion
     */
    public abstract Question createCopy();
}
