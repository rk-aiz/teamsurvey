package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
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
    public List<LoginUser> findAll() {
        return authenticationMapper.selectAll()
                .stream()
                .map(AuthenticationEntity::toModel)
                .toList();
    }

    @Override
    public LoginUser findByUsername(String username) {

        AuthenticationEntity entity = authenticationMapper.selectByUsername(username);

        if (entity == null)
            return null;

        List<UserGroup> groups = userGroupRepository.findByUsername(username);

        Collection<? extends GrantedAuthority> authorities = groups.stream().flatMap(
                g -> g.getAuthorityList().stream()).distinct().toList();

        LoginUser user = new LoginUser(
                entity.getUsername(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isEnabled(),
                authorities);

        user.setAssignedGroups(groups);
        user.setEmail(entity.getEmail());
        user.setDisplayName(entity.getDisplayName());
        return user;
    }

    @Override
    public List<LoginUser> findWithPaging(long offset, int pageSize) {
        List<AuthenticationEntity> entities = authenticationMapper.selectWithPaging(offset, pageSize);

        return entities.stream().map(entity -> {
            LoginUser loginUser = entity.toModel();
            loginUser.setAssignedGroups(entity.getAssignedGroups());
            return loginUser;
        }).toList();
    }

    @Override
    public long count() {
        return authenticationMapper.count();
    }

    @Override
    public boolean add(LoginUser user) {
        return authenticationMapper.insert(AuthenticationEntity.from(user)) > 0;
    }

    @Override
    public boolean set(LoginUser user) {
        return authenticationMapper.update(AuthenticationEntity.from(user)) > 0;
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
        return authenticationMapper.countByUsername(username) > 0;
    }
}
