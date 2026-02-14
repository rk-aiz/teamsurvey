package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;

public interface UserGroupService {

    /** 全てのユーザーグループを取得します */
    List<UserGroup> findAll();

    void save(UserGroup userGroup);

    boolean delete(Integer groupId);

    UserGroup getOrCreateSystemAdminGroup();
}