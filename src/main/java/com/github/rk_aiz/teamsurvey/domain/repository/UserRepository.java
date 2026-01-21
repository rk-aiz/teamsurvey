package com.github.rk_aiz.teamsurvey.domain.repository;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.User;

public interface UserRepository {

    List<User> findAll();

    User findByUsername(String username);

    void add(User user);

    void set(User user);

    void remove(String username);
}