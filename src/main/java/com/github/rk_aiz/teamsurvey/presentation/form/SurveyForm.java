package com.github.rk_aiz.teamsurvey.presentation.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.type.ResultVisibility;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;
import com.github.rk_aiz.teamsurvey.presentation.validation.CriticalNotNull;
import com.github.rk_aiz.teamsurvey.presentation.validation.OnClosedSurvey;
import com.github.rk_aiz.teamsurvey.presentation.validation.OnDeletedSurvey;
import com.github.rk_aiz.teamsurvey.presentation.validation.OnDraftSurvey;
import com.github.rk_aiz.teamsurvey.presentation.validation.OnPublishedSurvey;
import com.github.rk_aiz.teamsurvey.presentation.validation.OnSuspendedSurvey;
import com.github.rk_aiz.teamsurvey.presentation.validation.SurveyValidationGroup;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    /** ゲスト回答許可 */
    private boolean allowGuest;

    /** 集計結果の公開範囲 */
    private ResultVisibility resultVisibility;

    /** 締め切り日時 */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Future(message = "締め切り日時は未来の日時を指定してください")
    private LocalDateTime deadline;

    /** 質問リスト */
    @Builder.Default
    @Valid // @Validで、リストの中身(QuestionForm)もバリデーションする
    private List<QuestionForm> questions = new ArrayList<>();

    /** 公開対象グループIDリスト */
    @Builder.Default
    private List<Integer> groupIds = new ArrayList<>();

    /** 新規判定 */
    private boolean isNew;

    /**
     * Modelを受け取って、Formを返す静的メソッド
     */
    public static SurveyForm from(Survey model, boolean isNew) {
        SurveyFormBuilder builder = SurveyForm.builder()
                .title(model.getTitle())
                .isNew(isNew);

        /**
         * TODO 実装
         */
        return builder.build();
    }

    public Survey toModel() {
        Survey survey = new Survey();
        // questionsは型が違うため除外してコピー
        BeanUtils.copyProperties(this, survey, "questions");

        // QuestionForm -> Question への変換
        if (this.getQuestions() != null) {
            List<Question> questions = new ArrayList<>();
            int order = 1;
            for (QuestionForm qForm : this.getQuestions()) {
                Question q = qForm.toModel();
                q.setDisplayOrder(order++); // リストの順番通りに番号を振る
                questions.add(q);
            }
            survey.setQuestions(questions);
        }

        // GroupIds(List<Integer>) -> SelectedGroups(List<UserGroup>)
        if (this.getGroupIds() != null) {
            List<UserGroup> groups = new ArrayList<>();
            for (Integer id : this.getGroupIds()) {
                UserGroup g = new UserGroup();
                g.setGroupId(id);
                groups.add(g);
            }
            survey.setTargetGroups(groups);
        }
        return survey;
    }

    public Class<? extends SurveyValidationGroup> getValidationGroup() {
        return switch (this.getStatus()) {
            case DRAFT -> OnDraftSurvey.class;
            case PUBLISHED -> OnPublishedSurvey.class;
            case SUSPENDED -> OnSuspendedSurvey.class;
            case CLOSED -> OnClosedSurvey.class;
            case DELETED -> OnDeletedSurvey.class;
            default -> SurveyValidationGroup.class;
        };
    }
}
