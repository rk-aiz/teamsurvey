package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.model.question.FreeResponseQuestion;
import com.github.rk_aiz.teamsurvey.domain.model.question.MultipleChoiceQuestion;
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

    /**
     * Entity -> Domain Model 変換
     */
    public Question toModel() {
        Question question = switch (this.type) {
            case TEXT -> new FreeResponseQuestion();
            case RADIO -> new SingleChoiceQuestion();
            case CHECKBOX -> new MultipleChoiceQuestion();
        };

        BeanUtils.copyProperties(this, question, "questionId");
        question.setQuestionId(this.id);
        question.setSurveyId(this.surveyId);

        // 選択式の場合、AnswerOptionをセット
        if (question instanceof SingleChoiceQuestion scq && this.answerPattern != null) {
            AnswerOption option = new AnswerOption();
            BeanUtils.copyProperties(this.answerPattern, option);
            // 必要であればItemsの変換もここで行う
            scq.setAnswerOption(option);
        }

        return question;
    }

    /**
     * Domain Model -> Entity 変換
     */
    public static QuestionEntity fromModel(Question model) {
        QuestionEntity entity = QuestionEntity.builder()
                .id(model.getQuestionId())
                .surveyId(model.getSurveyId())
                .text(model.getText())
                .displayOrder(model.getDisplayOrder())
                .type(model.getType())
                .required(model.isRequired())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();

        if (model instanceof SingleChoiceQuestion scq && !scq.getAnswerOption().isEmpty()) {
            AnswerPatternEntity ap = new AnswerPatternEntity();
            ap.setId(scq.getAnswerOption().getId());
            entity.setAnswerPattern(ap);
        }
        return entity;
    }
}
