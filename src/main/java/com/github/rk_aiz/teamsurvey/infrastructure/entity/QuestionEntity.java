package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.question.FreeResponseQuestion;
import com.github.rk_aiz.teamsurvey.domain.model.question.MultiChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.model.question.SingleChoiceQuestion;
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
    private Integer surveyId;
    /** 回答パターン(結合用) */
    private Integer answerPatternId;
    /** 質問詳細 */
    private String text;
    /** 表示順序 */
    private Integer displayOrder;
    /** 回答モード */
    @Builder.Default
    private QuestionType type = QuestionType.RADIO;
    /** 必須回答かどうか */
    private boolean isRequired;
    /** 論理削除フラグ */
    private boolean isDeleted;

    /**
     * Entity -> Domain Model 変換
     */
    public Question toModel() {

        Question question = switch (this.type) {
            case RADIO -> new SingleChoiceQuestion();
            case CHECKBOX -> new MultiChoiceQuestion();
            case TEXT -> new FreeResponseQuestion();
        };

        BeanUtils.copyProperties(this, question);
        return question;
    }

    /**
     * Domain Model -> Entity 変換
     */
    public static QuestionEntity from(Question model) {
        QuestionEntity entity = new QuestionEntity();
        BeanUtils.copyProperties(model, entity);

        switch (model) {
            case SingleChoiceQuestion scq -> {
                entity.setAnswerPatternId(scq
                        .getAnswerOption()
                        .getAnswerOptionId());
            }
            default -> {
            }
        }

        return entity;
    }
}
