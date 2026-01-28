package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;

/*
 * 直接ドメインモデルを返すバイパス
 */
@Mapper
public interface LoginUserMapper {

    List<LoginUser> selectAll();
}
