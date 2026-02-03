package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.domain.model.result.QuestionAggregation;
import com.github.rk_aiz.teamsurvey.domain.model.result.QuestionAggregation.OptionStat;
import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;

@Mapper
public interface SurveyResultMapper {

    List<SurveyAggregation> selectAll();

    SurveyAggregation selectById(@Param("id") Integer id);

    List<QuestionAggregation> selectQuestionAggregations(@Param("surveyId") Integer surveyId);

    List<OptionStat> selectOptionStats(
        @Param("questionId") Integer questionId,
        @Param("answerPatternId") Integer answerPatternId,
        @Param("answerCount") Integer answerCount);
    
    List<String> selectTextAnswers(@Param("questionId") Integer questionId);

   
}
