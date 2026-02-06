package com.github.rk_aiz.teamsurvey.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.type.ResultVisibility;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * アンケートのヘッダー情報を表すクラス
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Survey {

    /** 主キー */
    private Integer id;

    /** アンケート名 */
    private String title;

    /** アンケート作成日時 */
    private LocalDateTime createdAt;
    /** アンケート更新日時 */
    private LocalDateTime updatedAt;

    /** 回答締め切り日時 (nullの場合は無期限) */
    private LocalDateTime deadline;

    /** アンケートの状態 */
    @Builder.Default
    private SurveyStatus status = SurveyStatus.DRAFT;

    /** 集計結果の公開範囲 */
    @Builder.Default
    private ResultVisibility resultVisibility = ResultVisibility.ADMIN_ONLY;

    /** アンケートに紐づく質問リスト */
    @Builder.Default
    private Map<Integer, Question> questions = new HashMap<>();

    /** 公開対象グループリスト */
    @Builder.Default
    private List<UserGroup> targetGroups = new ArrayList<>();

    /**
     * 集計結果が公開期間に入っているか（締め切りを過ぎているか）を判定します。
     * 
     * @return true: 公開期間中, false: まだ公開期間ではない
     */
    public boolean isResultPublished() {
        return this.deadline != null && this.deadline.isBefore(LocalDateTime.now());
    }

    /**
     * 現在回答を受け付けている状態かを判定します。
     * 
     * @return true: 回答可能, false: 回答不可
     */
    public boolean isResponsible() {
        return this.status == SurveyStatus.PUBLISHED;
    }

    /**
     * このアンケートを複製して新しいアンケート（下書き状態）を作成します。
     * 
     * @return 複製された新しいSurvey
     */
    public Survey toDraftCopy() {
        Survey copy = Survey.builder()
                .title(getTitle() + " (コピー)")
                .resultVisibility(getResultVisibility())
                .status(SurveyStatus.DRAFT) // 下書きに戻す
                .build();

        // 質問のコピー
        copy.setQuestions(getQuestions());
        
        // 対象グループのコピー
        copy.setTargetGroups(getTargetGroups());
        return copy;
    }

    /**
     * このアンケートが公開可能な状態か（設問設定などに不備がないか）を判定します。
     * 
     * @return true: 公開可能
     */
    public boolean canPublish() {
        Collection<Question> qs = this.getQuestions();

        if (qs.isEmpty()) {
            return false; // 質問がなければ公開不可
        }
        // 全ての質問についてチェック
        for (Question q : qs) {
            if (!q.isValidQuestion()) { // 構成に不備がある
                return false;
            }
        }
        return true;
    }

    public void setQuestions(Collection<Question> questions) {
        if (questions == null) {
            this.questions = new HashMap<>();
            return;
        }
        // Mapに変換してセットする
        this.questions = questions.stream()
                .collect(HashMap::new, (m, q) -> m.put(q.getId(), q), Map::putAll);
    }

    public void setQuestions(Map<Integer, Question> questions) {
        if (questions == null) {
            this.questions = new HashMap<>();
            return;
        }
        this.questions = questions;
    }

    /**
     * 設問リストを返します。nullの場合は空のリストを作成して返します。
     */
    public Collection<Question> getQuestions() {
        if (this.questions == null) {
            this.setQuestions(new HashMap<>());
        }
        return this.questions.values()
                .stream()
                .sorted(Comparator.comparingInt(q -> q.getDisplayOrder()))
                .toList();
    }

    /**
     * 設問リストを返します。nullの場合は空のリストを作成して返します。
     */
    public Question getQuestionById(Integer questionId) {
        if (this.questions == null) {
            return null;
        }

        return this.questions.get(questionId);
    }

    /**
     * 公開対象グループリストを設定します。
     * nullが渡された場合は空リストを設定します。
     */
    public void setTargetGroups(List<UserGroup> targetGroups) {
        this.targetGroups = (targetGroups != null) ? targetGroups : new ArrayList<>();
    }
}
