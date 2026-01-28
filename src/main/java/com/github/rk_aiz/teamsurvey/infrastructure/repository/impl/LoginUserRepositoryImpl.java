package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.AuthenticationEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.AuthenticationMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.LoginUserMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.UserGroupRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.LoginUserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LoginUserRepositoryImpl implements LoginUserRepository {
    
    private final AuthenticationMapper authenticationMapper;
    private final UserGroupRepository userGroupRepository;
    private final LoginUserMapper loginUserMapper;

    @Override
    public List<LoginUser> findAll() {
        return loginUserMapper.selectAll();
    }
    
    @Override
    public LoginUser findByUsername(String username) {
        
        AuthenticationEntity entity = 
                authenticationMapper.selectByUsername(username);

        if (entity == null) return null;

        List<UserGroup> groups = 
                userGroupRepository.findByUsername(username);

        Collection<? extends GrantedAuthority> authorities = groups.stream().flatMap(
            g -> g.getAuthorityList().stream()
        ).distinct().toList();

        LoginUser user = new LoginUser(
            entity.getUsername(),
            entity.getPassword(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.isEnabled(),
            authorities
        );

        user.setAssignedGroups(groups);
        user.setEmail(entity.getEmail());
        user.setDisplayName(entity.getDisplayName());
        return user;
    }

    @Override
    public void add(LoginUser user) {
        authenticationMapper.insert(AuthenticationEntity.from(user));
    }

    @Override
    public void set(LoginUser user) {
        authenticationMapper.update(AuthenticationEntity.from(user));
    }

    @Override
    public void remove(String username) {
        authenticationMapper.delete(username);
    }



}
