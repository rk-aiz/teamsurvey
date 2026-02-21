package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
        return surveyResultMapper.selectAll();
    }

    @Override
    public List<SurveyAggregation> findWithPagingByUserGroupIds(long offset, int pageSize, List<Integer> userGroupIds) {
        return surveyResultMapper.selectWithPagingByUserGroupIds(offset, pageSize, userGroupIds);
    }

    @Override
    public SurveyAggregation findBySurveyId(Integer surveyId) {
        return surveyResultMapper.selectById(surveyId);
    }

    @Override
    public void steamCsvWithConsumerBySurveyId(
            Integer surveyId, Consumer<Map<String, Object>> rowConsumer) {
        // ResultHandlerをここでラップして隠ぺいする
        surveyResultMapper.streamForCsv(surveyId, resultContext -> {
            Map<String, Object> row = resultContext.getResultObject();
            rowConsumer.accept(row);
        });
    }

    @Override
    public long countByUserGroupIds(List<Integer> userGroupIds) {
        return surveyResultMapper.countByUserGroupIds(userGroupIds);
    }
}
