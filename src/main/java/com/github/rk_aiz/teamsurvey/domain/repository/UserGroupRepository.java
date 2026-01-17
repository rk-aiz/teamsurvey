package com.github.rk_aiz.teamsurvey.domain.repository;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;

public interface UserGroupRepository {

    List<UserGroup> findAll();

    UserGroup findById(Integer id);

    /**
     * 指定されたユーザーが所属するグループの一覧を取得します。
     * (Active DirectoryのmemberOf属性に相当する逆引き検索)
     * 
     * @param username ユーザーID
     */
    List<UserGroup> findByUsername(String username);
}