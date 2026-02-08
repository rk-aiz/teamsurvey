package com.github.rk_aiz.teamsurvey.application.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.github.rk_aiz.teamsurvey.application.validation.CriticalNotNull;
import com.github.rk_aiz.teamsurvey.application.validation.OnPublishedSurvey;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.type.ResultVisibility;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyForm {

    /** Survey ID */
    @CriticalNotNull(groups = OnPublishedSurvey.class)
    private Integer id;

    /** title */
    @NotBlank(message = "Titleは必須です")
    @Size(max = 255, message = "Titleは{max}文字以内で入力してください")
    private String title;

    /** ステータス */
    private SurveyStatus status;

    /** 集計結果の公開範囲 */
    private ResultVisibility resultVisibility;

    /** 締め切り日時 */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Future(message = "締め切り日時は未来の日時を指定してください")
    private LocalDateTime deadline;

    /** 質問リスト */
    @Builder.Default
    @Valid // @Validで、リストの中身(QuestionForm)もバリデーションする
    private List<QuestionForm> questionForms = new ArrayList<>();

    /** 公開対象グループリスト */
    @Builder.Default
    private List<UserGroup> targetGroups = new ArrayList<>();

    /** 新規判定 */
    private boolean isNew;

    /**
     * Model -> Form
     */
    public static SurveyForm from(Survey model, boolean isNew) {
        SurveyForm form = new SurveyForm();
        BeanUtils.copyProperties(model, form, "questions");

        form.setNew(isNew);

        Optional.ofNullable(model.getQuestions())
                .ifPresent(questions -> form.setQuestionForms(questions.stream().map(QuestionForm::from).toList()));

        return form;
    }

    /*
     * Form -> Model
     */
    public Survey toModel() {
        Survey survey = new Survey();
        // questionsは型が違うため除外してコピー
        BeanUtils.copyProperties(this, survey, "questionForms");

        // リストの順番通りにdisplayOrderをセット
        int order = 1;
        for (QuestionForm qf : this.getQuestionForms()) {
            qf.setDisplayOrder(order++);
        }

        // List<QuestionForm> -> List<Question> へ変換して、セッターに渡す
        Optional.ofNullable(this.getQuestionForms()).ifPresent(qForms -> survey.setQuestions(
                qForms.stream()
                        .filter(Objects::nonNull)
                        .map(QuestionForm::toModel)
                        .filter(Objects::nonNull) // toModelがnullを返す可能性があるため、ここでもフィルタリングする
                        .toList()));

        return survey;
    }
}
