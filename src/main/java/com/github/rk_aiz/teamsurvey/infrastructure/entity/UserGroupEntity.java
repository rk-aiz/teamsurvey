package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.type.Authority;

import lombok.Data;

@Data
public class UserGroupEntity {
    private Integer id;
    private String groupName;
    private Authority authority;
    private boolean isSystemGroup;

    public UserGroup toModel() {
        UserGroup model = new UserGroup();
        BeanUtils.copyProperties(this, model);
        return model;
    }

    public static UserGroupEntity from(UserGroup model) {
        UserGroupEntity entity = new UserGroupEntity();
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}
