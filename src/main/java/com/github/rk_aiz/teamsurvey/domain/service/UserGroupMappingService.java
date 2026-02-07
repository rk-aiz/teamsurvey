package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.List;

public interface UserGroupMappingService {

    boolean save(String username, List<Integer> groupIds);
}