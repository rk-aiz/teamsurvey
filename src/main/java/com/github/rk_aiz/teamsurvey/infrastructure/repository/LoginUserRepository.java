package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;

public interface LoginUserRepository {

    List<LoginUser> findAll();

    LoginUser findByUsername(String username);

    void add(LoginUser user);

    void set(LoginUser user);

    void remove(String username);
}