package com.github.rk_aiz.teamsurvey.infrastructure.repository_impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.repository.UserGroupRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.UserGroupEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.UserGroupMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserGroupRepositoryImpl implements UserGroupRepository {
    
    private final UserGroupMapper userGroupMapper;

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
