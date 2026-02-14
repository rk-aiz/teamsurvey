package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.AuthenticationEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.AuthenticationMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.AccountRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.UserGroupRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AuthenticationMapper authenticationMapper;
    private final UserGroupRepository userGroupRepository;

    @Override
    public List<UserAccount> findAll() {
        return authenticationMapper.selectAll().stream().map(AuthenticationEntity::toModel).toList();
    }

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return Optional
                .ofNullable(authenticationMapper.selectByUsername(username))
                .map(entity -> entity.toModel());
    }

    @Override
    public List<UserAccount> findWithPaging(long offset, int pageSize) {
        List<AuthenticationEntity> entities = authenticationMapper.selectWithPaging(offset, pageSize);
        return entities.stream().map(AuthenticationEntity::toModel).toList();
    }

    @Override
    public long count() {
        return authenticationMapper.count();
    }

    @Override
    @Transactional
    public boolean save(UserAccount user) {
        boolean success;
        if (this.exists(user.username())) {
            success = 0 < this.authenticationMapper
                    .update(AuthenticationEntity.from(user));
        } else {
            success = 0 < this.authenticationMapper
                    .insert(AuthenticationEntity.from(user));
        }

        if (!success)
            return false;

        // グループの紐付け情報も保存する
        List<Integer> groupIds = user.assignedGroups().stream()
                .map(UserGroup::getId)
                .toList();

        

        userGroupRepository.updateUserGroupMapping(user.username(), groupIds);

        return true;
    }

    @Override
    public boolean remove(String username) {
        return authenticationMapper.delete(username) > 0;
    }

    @Override
    public long countEnabledAdmins() {
        return authenticationMapper.countEnabledAdmins();
    }

    @Override
    public boolean exists(String username) {
        return authenticationMapper.exists(username);
    }
}
