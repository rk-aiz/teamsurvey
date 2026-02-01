package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.UserGroupEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.SurveyTargetGroupMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.UserGroupMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.UserGroupRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserGroupRepositoryImpl implements UserGroupRepository {

    private final UserGroupMapper userGroupMapper;
    private final SurveyTargetGroupMapper surveyTargetGroupMapper;

    @Override
    public List<UserGroup> findAll() {
        return userGroupMapper.selectAll()
                .stream().map(UserGroupEntity::toModel).toList();
    }

    @Override
    public UserGroup findById(Integer groupId) {
        return userGroupMapper.selectById(groupId).toModel();
    }

    @Override
    public List<UserGroup> findByUsername(String username) {
        return userGroupMapper.selectByUsername(username)
                .stream().map(UserGroupEntity::toModel).toList();
    }

    @Override
    public List<UserGroup> findBySurveyId(Integer surveyId) {
        return surveyTargetGroupMapper.selectGroupIdBySurveyId(surveyId).stream()
                .map(this::findById).toList();
    }

    @Override
    public void add(UserGroup group) {
        userGroupMapper.insert(UserGroupEntity.from(group));
    }

    @Override
    public void set(UserGroup group) {
        userGroupMapper.update(UserGroupEntity.from(group));
    }

    @Override
    public void remove(Integer groupId) {
        userGroupMapper.delete(groupId);
    }

}
