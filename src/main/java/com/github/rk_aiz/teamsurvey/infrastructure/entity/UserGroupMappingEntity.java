package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import lombok.Data;

@Data
public class UserGroupMappingEntity {
    private String username;
    private Integer groupId;
}