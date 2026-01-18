package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.QuestionEntity;

/**
 * MyBatisが自動生成する実装クラスがそのままRepositoryの実装になります。
 */
@Mapper
public interface QuestionMapper {
    /**
     * 全ての質問を取得します
     */
    List<QuestionEntity> selectAll();

    /**
     * 指定されたIDに対する質問を取得します
     */
    QuestionEntity selectById(@Param("id") Integer id);

    /**
     * 指定されたアンケートIDに紐づく質問一覧を取得します
     */
    List<QuestionEntity> selectBySurveyId(@Param("surveyId") Integer surveyId);

    /**
     * 新しい質問を登録します
     */
    void insert(QuestionEntity question);

    /**
     * 質問を更新します
     */
    void update(QuestionEntity question);

    /**
     * 質問を削除します
     */
    void delete(@Param("id") Integer id);
}
