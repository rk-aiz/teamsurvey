package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.UserGroupEntity;

@Mapper
public interface UserGroupMapper {

    @Select("SELECT * FROM user_groups")
    List<UserGroupEntity> selectAll();

    @Select("SELECT * FROM user_groups WHERE id = #{id}")
    UserGroupEntity selectById(@Param("id") Integer id);

    @Insert("INSERT INTO user_groups (group_name) VALUES (#{groupName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserGroupEntity userGroup);

    @Update("UPDATE user_groups SET group_name = #{groupName} WHERE id = #{id}")
    void update(UserGroupEntity userGroup);

    @Delete("DELETE FROM user_groups WHERE id = #{id}")
    void delete(@Param("id") Integer id);
}
