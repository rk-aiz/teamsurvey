package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.SurveyTargetGroupMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyTargetGroupRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SurveyTargetGroupRepositoryImpl implements SurveyTargetGroupRepository {

    private final SurveyTargetGroupMapper surveyTargetGroupMapper;

    @Override
    public List<Integer> findByGroupId(Integer groupId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByGroupId'");
    }

    @Override
    public List<Integer> findBySurveyId(Integer surveyId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBySurveyId'");
    }

    @Override
    public void add(Integer surveyId, Integer groupId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void remove(Integer surveyId, Integer groupId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

}
