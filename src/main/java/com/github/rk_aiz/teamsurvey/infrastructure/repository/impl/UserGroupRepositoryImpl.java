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
    public boolean add(UserGroup group) {
        return userGroupMapper.insert(UserGroupEntity.from(group)) > 0;
    }

    @Override
    public boolean set(UserGroup group) {
        return userGroupMapper.update(UserGroupEntity.from(group)) > 0;
    }

    @Override
    public boolean remove(Integer groupId) {
        return userGroupMapper.delete(groupId) > 0;
    }

}
