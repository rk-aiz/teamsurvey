package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;

public interface AccountRepository {

    List<LoginUser> findAll();

    LoginUser findByUsername(String username);

    List<LoginUser> findWithPaging(long offset, int pageSize);

    long count();

    boolean add(LoginUser user);

    boolean set(LoginUser user);

    boolean remove(String username);

    long countEnabledAdmins();

    boolean exists(String username);
}