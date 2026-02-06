package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.QuestionMapper;

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
     * 指定されたアンケートIDに紐づく設問一覧を取得します
     */
    List<Question> findBySurveyId(Integer surveyId);

    /**
     * 新しい設問を登録します
     */
    boolean add(Question question);

    /**
     * 設問を更新します
     */
    boolean set(Question question);

    /**
     * 設問を削除します
     */
    boolean remove(Integer id);
    
    /**
     * 指定されたアンケートIDに紐づく設問を削除します
     */
    boolean removeBySurveyId(@Param("surveyId") Integer surveyId);
}
