package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.Collection;
import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;

public interface SurveyResultService {

    /** 全てのアンケート集計を取得します */
    List<SurveyAggregation> findAllSurveyAggregations();

    /**
     * アンケート集計を取得します。
     * 
     * @throws IllegalArgumentException アンケートが存在しない場合
     */
    SurveyAggregation findSurveyAggregationById(Integer surveyId) throws IllegalArgumentException;

    /**
     * 
     * @param userGroupIds
     * @return
     */
    List<SurveyAggregation> findAggregationsWithPagingByUserGroups(Collection<UserGroup> userGroupIds);

    /**
     * 指定されたアンケートの回答データをCSV形式で生成します。
     */
    String generateCsv(Integer surveyId);

}