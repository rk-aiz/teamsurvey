package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.AuthenticationEntity;

@Mapper
public interface AuthenticationMapper {

    List<AuthenticationEntity> selectAll();

    List<AuthenticationEntity> findWithPaging(long offset, int limit);
    
    AuthenticationEntity selectByUsername(@Param("username") String username);
    
    void insert(AuthenticationEntity authentication);

    void update(AuthenticationEntity anthentication);

    void delete(@Param("username")String username);
    
    long count();
}
