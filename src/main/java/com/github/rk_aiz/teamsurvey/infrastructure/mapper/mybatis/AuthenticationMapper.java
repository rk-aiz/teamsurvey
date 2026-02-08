package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.AuthenticationEntity;

@Mapper
public interface AuthenticationMapper {

    List<AuthenticationEntity> selectAll();

    List<AuthenticationEntity> selectWithPaging(@Param("offset") long offset, @Param("limit") int limit);

    AuthenticationEntity selectByUsername(@Param("username") String username);

    int insert(AuthenticationEntity authentication);

    int update(AuthenticationEntity authentication);

    int delete(@Param("username") String username);

    long count();

    long countEnabledAdmins();

    boolean exists(@Param("username") String username);
}
