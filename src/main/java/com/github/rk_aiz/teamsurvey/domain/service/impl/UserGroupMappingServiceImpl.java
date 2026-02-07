package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.service.UserGroupMappingService;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.UserGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserGroupMappingServiceImpl implements UserGroupMappingService {

    /** DI */
    private final UserGroupRepository userGroupRepository;

    @Override
    public boolean save(String username, List<Integer> groupIds) {
        return userGroupRepository.updateUserGroupMapping(username, groupIds);
    }
}