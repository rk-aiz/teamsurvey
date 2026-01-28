package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.question.Question;

/**
 * ユースケース -> {@link QuestionSurvice}
 * Mybatis -> {@link QuestionMapper}
 */
public interface QuestionRepository {
    /**
     * 全ての質問を取得します
     */
    List<Question> findAll();

    /**
     * 指定されたIDに対する質問を取得します
     */
    Question findById(Integer id);

    /**
     * 指定されたアンケートIDに紐づく質問一覧を取得します
     */
    List<Question> findBySurveyId(Integer surveyId);

    /**
     * 新しい質問を登録します
     */
    void add(Question question);

    /**
     * 質問を更新します
     */
    void set(Question question);

    /**
     * 質問を削除します
     */
    void remove(Integer id);
}
