package com.github.rk_aiz.teamsurvey.application.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.github.rk_aiz.teamsurvey.application.validation.CriticalNotNull;
import com.github.rk_aiz.teamsurvey.application.validation.OnPublishedSurvey;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.type.ResultVisibility;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private Integer surveyId;

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
     * Modelを受け取って、Formを返す静的メソッド
     */
    public static SurveyForm from(Survey model, boolean isNew) {
        SurveyForm form = new SurveyForm();
        BeanUtils.copyProperties(model, form, "questions");

        // 名前が異なるIDの詰め替え
        // form.setSurveyId(model.getSurveyId());
        form.setNew(isNew);

        // 質問リストの手動マッピング
        if (model.getQuestions() != null) {
            List<QuestionForm> questionForms = new ArrayList<>();
            for (Question question : model.getQuestions()) {
                QuestionForm qf = new QuestionForm();
                BeanUtils.copyProperties(question, qf);
                qf.setId(question.getQuestionId());
                questionForms.add(qf);
            }
            form.setQuestionForms(questionForms);
        }

        log.info(form.getStatus().toString());

        return form;
    }

    public Survey toModel() {
        Survey survey = new Survey();
        // questionsは型が違うため除外してコピー
        BeanUtils.copyProperties(this, survey, "questions");
        survey.setSurveyId(this.getSurveyId());

        // QuestionForm -> Question への変換
        if (this.getQuestionForms() != null) {
            List<Question> questions = new ArrayList<>();
            int order = 1;
            for (QuestionForm qForm : this.getQuestionForms()) {
                Question q = qForm.toModel();
                q.setDisplayOrder(order++); // リストの順番通りに番号を振る
                questions.add(q);
            }
            survey.setQuestions(questions);
        }

        return survey;
    }
}
