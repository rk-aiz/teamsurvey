package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;

@Mapper
public interface UserGroupMapper {

    List<UserGroup> findAll();

    UserGroup findById(Integer id);

    /**
     * 指定されたユーザーが所属するグループの一覧を取得します。
     * 
     * @param username ユーザーID
     * @return グループリスト
     */
    List<UserGroup> findByUsername(String username);
}