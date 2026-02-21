package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.result.QuestionAggregation;
import com.github.rk_aiz.teamsurvey.domain.model.result.QuestionAggregation.OptionStat;
import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;

@Mapper
public interface SurveyResultMapper {

    List<SurveyAggregation> selectAll();

    SurveyAggregation selectById(@Param("id") Integer id);

    /**
     * ユーザーグループのリストからページネーション付きでアンケートを取得します
     *
     * @param offset
     * @param limit
     * @param userGroupIds
     * @return
     */
    List<SurveyAggregation> selectWithPagingByUserGroupIds(
            @Param("offset") long offset,
            @Param("limit") int limit, 
            @Param("userGroupIds") List<Integer> userGroupIds);

    List<QuestionAggregation> selectQuestionAggregations(@Param("surveyId") Integer surveyId);

    List<OptionStat> selectOptionStats(
            @Param("questionId") Integer questionId,
            @Param("answerPatternId") Integer answerPatternId,
            @Param("answerCount") Integer answerCount);

    List<String> selectTextAnswers(@Param("questionId") Integer questionId);

    List<Response> selectResponsesForCsv(@Param("surveyId") Integer surveyid);
}
