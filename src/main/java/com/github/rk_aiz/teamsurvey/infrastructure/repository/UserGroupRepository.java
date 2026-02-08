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

    boolean existsByGroupName(String groupName);

    boolean save(UserGroup group);

    boolean remove(Integer groupId);

    void updateUserGroupMapping(String username, List<Integer> groupIds);

}