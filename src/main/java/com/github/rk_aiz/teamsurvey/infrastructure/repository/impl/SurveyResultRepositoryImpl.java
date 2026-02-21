package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.SurveyResultMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyResultRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class SurveyResultRepositoryImpl implements SurveyResultRepository {

    private final SurveyResultMapper surveyResultMapper;

    @Override
    public List<SurveyAggregation> findAll() {
        return this.surveyResultMapper.selectAll();
    }

    @Override
    public List<SurveyAggregation> findWithPagingByUserGroupIds(long offset, int pageSize, List<Integer> userGroupIds) {
        return surveyResultMapper.selectWithPagingByUserGroupIds(offset, pageSize, userGroupIds);
    }

    @Override
    public SurveyAggregation findBySurveyId(Integer surveyId) {
        return this.surveyResultMapper.selectById(surveyId);
    }

    @Override
    public List<Response> findResponsesForCsv(Integer surveyId) {
        return this.surveyResultMapper.selectResponsesForCsv(surveyId);
    }
}
