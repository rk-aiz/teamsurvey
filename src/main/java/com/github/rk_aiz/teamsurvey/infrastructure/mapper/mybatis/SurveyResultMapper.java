package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

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

    /**
     * 設問ごとの集計情報を取得
     */
    List<QuestionAggregation> selectQuestionAggregations(@Param("surveyId") Integer surveyId);

    /**
     * 選択肢ごとの統計を取得 (件数と割合)
     */
    List<OptionStat> selectOptionStats(
            @Param("questionId") Integer questionId,
            @Param("answerPatternId") Integer answerPatternId,
            @Param("answerCount") Integer answerCount);

    /**
     * 自由記述回答を取得
     */
    List<String> selectTextAnswers(@Param("questionId") Integer questionId);

    /**
     * CSV出力用: 回答一覧取得
     */
    void streamForCsv(
            @Param("surveyId") Integer surveyId, 
            ResultHandler<Map<String, Object>> handler);

    /**
     * ユーザーグループが閲覧可能なアンケート件数を取得
     */
    long countByUserGroupIds(@Param("userGroupIds") List<Integer> userGroupIds);
}
