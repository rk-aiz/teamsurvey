package com.github.rk_aiz.teamsurvey.domain.model;

import com.github.rk_aiz.teamsurvey.domain.type.Authority;

import lombok.Data;

@Data
public class UserGroup {
    private Integer groupId;
    private String groupName;
    private Authority authority;
}