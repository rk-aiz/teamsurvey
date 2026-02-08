package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.List;

public interface UserGroupMappingService {

    void save(String username, List<Integer> groupIds);
}