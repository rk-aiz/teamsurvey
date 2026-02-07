package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserGroupMappingMapper {

    int insertBulk(@Param("username") String username, @Param("groupIds") List<Integer> gruopIds);

    int deleteByUsername(@Param("username") String username);
}
