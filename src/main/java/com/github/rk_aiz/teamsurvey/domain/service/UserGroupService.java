package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;

public interface UserGroupService {

    /** 全てのユーザーグループを取得します */
    List<UserGroup> findAll();

    boolean save(LoginUser loginUser);

    boolean delete(Integer groupId);
}