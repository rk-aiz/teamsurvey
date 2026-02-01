package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;

public interface UserGroupRepository {

    List<UserGroup> findAll();

    UserGroup findById(Integer groupId);

    /**
     * 指定されたユーザーが所属するグループの一覧を取得します。
     * 
     * @param username ユーザーID
     */
    List<UserGroup> findByUsername(String username);

    List<UserGroup> findBySurveyId(Integer surveyId);

    void add(UserGroup group);

    void set(UserGroup group);

    void remove(Integer groupId);
}