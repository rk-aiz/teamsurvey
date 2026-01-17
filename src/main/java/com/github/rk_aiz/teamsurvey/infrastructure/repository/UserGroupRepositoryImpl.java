package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.repository.UserGroupRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.UserGroupMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserGroupRepositoryImpl implements UserGroupRepository {

    private final UserGroupMapper userGroupMapper;

    @Override
    public List<UserGroup> findAll() {
        return userGroupMapper.findAll();
    }

    @Override
    public UserGroup findById(Integer id) {
        return userGroupMapper.findById(id);
    }

    @Override
    public List<UserGroup> findByUsername(String username) {
        return userGroupMapper.findByUsername(username);
    }
}