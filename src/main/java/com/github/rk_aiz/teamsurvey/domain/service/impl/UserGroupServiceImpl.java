package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.service.UserGroupService;
import com.github.rk_aiz.teamsurvey.domain.type.Authority;
import com.github.rk_aiz.teamsurvey.exception.ServiceRuleException;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.UserGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    /** DI */
    private final UserGroupRepository userGroupRepository;

    @Override
    public List<UserGroup> findAll() {
        return userGroupRepository.findAll();
    }

    @Override
    public void save(UserGroup userGroup) {

        if (userGroup.getId() == null) {
            // 新規登録時のチェック
            boolean isDuplicate = this.userGroupRepository
                    .existsByGroupName(userGroup.getGroupName());

            if (isDuplicate) {
                throw new DuplicateKeyException("指定されたグループ名は既に使用されています。");
            }
        } else {
            UserGroup currentDbGroup = this.userGroupRepository
                    .findById(userGroup.getId());

            // システムグループの場合は名称の変更のみ許可
            if (currentDbGroup.isSystemGroup() &&
                    currentDbGroup.getAuthority() != userGroup.getAuthority()) {
                throw new ServiceRuleException("システムグループは権限の変更はできません");
            }
        }

        userGroupRepository.save(userGroup);
    }

    @Override
    public boolean delete(Integer groupId) {
        UserGroup targetGroup = this.userGroupRepository.findById(groupId);

        // グループが存在しない、またはシステムグループの場合は削除不可
        if (targetGroup == null || targetGroup.isSystemGroup()) {
            return false;
        }

        return userGroupRepository.remove(groupId);
    }

    @Override
    public UserGroup getOrCreateSystemAdminGroup() {
        return this.userGroupRepository.findAll().stream()
                .filter(group -> group.getAuthority() == Authority.ADMIN && group.isSystemGroup())
                .findFirst()
                .orElseGet(() -> {
                    UserGroup newGroup = new UserGroup();
                    newGroup.setGroupName("システム管理者");
                    newGroup.setAuthority(Authority.ADMIN);
                    newGroup.setSystemGroup(true);
                    save(newGroup);
                    return newGroup;
                });
    }
}