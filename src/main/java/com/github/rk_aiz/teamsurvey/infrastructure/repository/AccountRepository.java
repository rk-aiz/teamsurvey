package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;

public interface AccountRepository {

    // List<UserAccount> findAll();

    UserAccount findByUsername(String username);

    List<UserAccount> findWithPaging(long offset, int pageSize);

    long count();

    boolean save(UserAccount user);

    boolean remove(String username);

    long countEnabledAdmins();

    boolean exists(String username);
}