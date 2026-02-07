package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.service.SurveyTargetGroupService;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyTargetGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyTargetGroupServiceImpl implements SurveyTargetGroupService {

    /** DI */
    private final SurveyTargetGroupRepository surveyTargetGroupRepository;

    @Override
    public boolean save(Integer surveyId, List<Integer> groupIds) {
        return this.surveyTargetGroupRepository.updateTargetGroups(surveyId, groupIds);
    }
}