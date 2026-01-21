package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.UserGroupEntity;

@Mapper
public interface UserGroupMapper {

    List<UserGroupEntity> selectAll();

    UserGroupEntity selectById(@Param("id") Integer id);

    List<UserGroupEntity> selectByUsername(@Param("username") String username);

    void insert(UserGroupEntity userGroup);
    
    void update(UserGroupEntity userGroup);

    void delete(@Param("id") Integer id);
}
