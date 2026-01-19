package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.type.Authority;

import lombok.Data;

@Data
public class UserGroupEntity {
    private Integer id;
    private String groupName;
    private Authority authority;

    public UserGroup toModel() {
        UserGroup model = new UserGroup();
        model.setGroupId(this.id);
        model.setGroupName(this.groupName);
        return model;
    }

    public static UserGroupEntity from(UserGroup model) {
        UserGroupEntity entity = new UserGroupEntity();
        entity.setId(model.getGroupId());
        entity.setGroupName(model.getGroupName());
        return entity;
    }
}
