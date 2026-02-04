package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.service.UserGroupService;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.UserGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {
    
    /** DI */
    private final UserGroupRepository userGroupRepository;
    
    @Override
    public List<UserGroup> findAll() {
        return userGroupRepository.findAll();
    }

    @Override
    public boolean save(LoginUser loginUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public boolean delete(Integer groupId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}