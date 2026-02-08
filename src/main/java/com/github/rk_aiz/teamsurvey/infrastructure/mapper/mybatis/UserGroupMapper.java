package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.UserGroupEntity;

@Mapper
public interface UserGroupMapper {

    List<UserGroupEntity> selectAll();

    UserGroupEntity selectById(@Param("id") Integer id);

    boolean existsByGroupName(@Param("groupName") String groupName);

    List<UserGroupEntity> selectByUsername(@Param("username") String username);

    int insert(UserGroupEntity userGroup);

    int update(UserGroupEntity userGroup);

    int delete(@Param("id") Integer id);
}
