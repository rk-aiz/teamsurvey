package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.List;

public interface SurveyTargetGroupService {

    List<Integer> findGroupIdBySurveyId(Integer surveyId);

    List<Integer> findSurveyIdByGroupId(Integer groupId);

    boolean save(Integer surveyId, Integer groupId);

    boolean remove(Integer surveyId, Integer groupId);
}