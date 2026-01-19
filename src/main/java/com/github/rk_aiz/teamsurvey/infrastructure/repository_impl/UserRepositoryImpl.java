package com.github.rk_aiz.teamsurvey.infrastructure.repository_impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.User;
import com.github.rk_aiz.teamsurvey.domain.repository.UserGroupRepository;
import com.github.rk_aiz.teamsurvey.domain.repository.UserRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.AuthenticationEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.AuthenticationMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    
    private final AuthenticationMapper authenticationMapper;
    private final UserGroupRepository userGroupRepository;


    @Override
    public List<User> findAll() {
        return authenticationMapper.selectAll()
                .stream().map(AuthenticationEntity::toModel).toList();
    }

    @Override
    public User findByUsername(String username) {
        User user = authenticationMapper.selectByUsername(username).toModel();
        user.setAssignedGroups(userGroupRepository.findByUsername(username));
        return user;
    }

    @Override
    public void add(User user) {
        authenticationMapper.insert(AuthenticationEntity.from(user));
    }

    @Override
    public void set(User user) {
        authenticationMapper.update(AuthenticationEntity.from(user));
    }

    @Override
    public void remove(String username) {
        authenticationMapper.delete(username);
    }



}
