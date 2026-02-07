package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.SurveyTargetGroupMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyTargetGroupRepository;
import com.github.rk_aiz.teamsurvey.util.ListUtils;

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
    public boolean remove(Integer surveyId, Integer groupId) {
        return surveyTargetGroupMapper.delete(surveyId, groupId) > 0;
    }

    @Override
    public boolean removeBySurveyId(Integer surveyId) {
        return surveyTargetGroupMapper.deleteBySurveyId(surveyId) > 0;
    }

    @Override
    public boolean updateTargetGroups(Integer surveyId, List<Integer> groupIds) {

        // 現在のグループIDリストを取得
        List<Integer> currentGroupIds = this.findBySurveyId(surveyId).stream()
                .sorted()
                .toList();

        // 新しいグループIDリストを整理
        List<Integer> newGroupIds = (groupIds == null ? List.<Integer>of() : groupIds).stream()
                .distinct()
                .sorted()
                .toList();

        // 変更がないなら即return
        if (currentGroupIds.equals(newGroupIds)) {
            return false;
        }

        if (!currentGroupIds.isEmpty()) {
            this.surveyTargetGroupMapper.deleteBySurveyId(surveyId);
        }
        if (!newGroupIds.isEmpty()) {
            // バルクインサートは念のためバッチ処理
            for (List<Integer> batch : ListUtils.partition(groupIds, 1000)) {
                this.surveyTargetGroupMapper.insertBulk(surveyId, batch);
            }
        }
        return true;
    }

}
