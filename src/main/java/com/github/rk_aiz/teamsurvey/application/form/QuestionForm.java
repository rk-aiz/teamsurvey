package com.github.rk_aiz.teamsurvey.application.form;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.model.question.FreeResponseQuestion;
import com.github.rk_aiz.teamsurvey.domain.model.question.MultipleChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.model.question.SingleChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.type.QuestionType;
import com.github.rk_aiz.teamsurvey.domain.util.StringUtils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionForm {

    private Integer id;

    @Size(max = 200, message = "質問文は{max}文字以内で入力してください")
    @NotBlank(message = "質問文は必須です")
    private String text;

    @NotNull(message = "設問の種類を指定してください")
    private QuestionType type;

    private boolean required;

    // ここからanserOptionの内容を編集することはないのでModelを利用
    private AnswerOption answerOption;

    private Integer displayOrder;

    /**
     * ThymeleafのFormBindingでNPEを防ぐためのGetter
     */
    public AnswerOption getAnswerOption() {
        return this.answerOption;
    }

    /**
     * この質問の設定が完了しているか（公開可能か）を判定します。
     */
    public boolean isConfigured() {
        // 質問文が空の場合は設定完了とみなさない
        if (!StringUtils.hasText(this.getText())) {
            return false;
        }

        // 自由記述(TEXT)なら回答パターンは不要なのでOK
        if (this.type == QuestionType.TEXT) {
            return true;
        }
        // 選択式(RADIO/CHECKBOX)の場合、回答パターンが設定(IDが存在)されている必要がある
        return !getAnswerOption().isEmpty();
    }

    public Question toModel() {
        Question question = switch (this.getType()) {
            case TEXT -> new FreeResponseQuestion();
            case RADIO -> new SingleChoiceQuestion();
            case CHECKBOX -> new MultipleChoiceQuestion();
        };
        BeanUtils.copyProperties(this, question);
        question.setQuestionId(id);
        return question;
    }

    public QuestionForm from(Question question) {
        QuestionForm form = new QuestionForm();
        BeanUtils.copyProperties(question, form);
        form.setId(id);
        return form;
    }
}