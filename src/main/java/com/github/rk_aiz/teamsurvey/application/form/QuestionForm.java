package com.github.rk_aiz.teamsurvey.application.form;

import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.application.validation.OnDraftSurvey;
import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.model.question.FreeResponseQuestion;
import com.github.rk_aiz.teamsurvey.domain.model.question.MultiChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.model.question.SingleChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.type.QuestionType;
import com.github.rk_aiz.teamsurvey.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionForm {

    private Integer id;

    @Size(max = 200, message = "質問文は{max}文字以内で入力してください", groups = OnDraftSurvey.class)
    @NotBlank(message = "質問文は必須です", groups = OnDraftSurvey.class)
    private String text;

    @NotNull(message = "設問の種類を指定してください", groups = OnDraftSurvey.class)
    private QuestionType type;

    private boolean required;

    // ここからanserOptionの内容を編集することはないのでDomain Modelを利用
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

    public Optional<Question> toModel() {

        Optional<Question> question = Optional.ofNullable(this.getType()).map(type -> switch (this.getType()) {
            case TEXT -> new FreeResponseQuestion();
            case RADIO -> new SingleChoiceQuestion();
            case CHECKBOX -> new MultiChoiceQuestion();
        });
        question.ifPresent(q -> BeanUtils.copyProperties(this, q));
        return question;
    }

    public static QuestionForm from(Question question) {
        QuestionForm form = new QuestionForm();

        BeanUtils.copyProperties(question, form);
        form.setType(switch (question) {
            case MultiChoiceQuestion q -> QuestionType.CHECKBOX;
            case SingleChoiceQuestion q -> QuestionType.RADIO;
            default -> QuestionType.TEXT;
        });

        return form;
    }
}