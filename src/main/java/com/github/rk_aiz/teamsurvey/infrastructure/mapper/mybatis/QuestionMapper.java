package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.QuestionEntity;

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

    int insert(QuestionEntity question);

    int update(QuestionEntity question);

    int delete(@Param("id") Integer id);
    
    int deleteBySurveyId(@Param("surveyId") Integer surveyId);
}
