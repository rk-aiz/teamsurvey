package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;

/**
 * アンケート情報の永続化・検索を行うリポジトリのインターフェース。
 */
public interface SurveyResultRepository {

    List<SurveyAggregation> findAll();

    List<SurveyAggregation> findWithPagingByUserGroupIds(
            long offset, int pageSize, List<Integer> userGroupIds);

    SurveyAggregation findBySurveyId(Integer surveyId);

    void steamCsvWithConsumerBySurveyId(
            Integer surveyId, Consumer<Map<String, Object>> rowConsumer);

    long countByUserGroupIds(List<Integer> userGroupIds);
}