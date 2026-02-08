package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.List;

public interface SurveyTargetGroupService {
    void save(Integer surveyId, List<Integer> groupIds);
}