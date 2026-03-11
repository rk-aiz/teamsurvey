package com.github.rk_aiz.teamsurvey.application.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.github.rk_aiz.teamsurvey.application.form.AccountForm;
import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;

@Mapper(componentModel = "spring")
public interface AccountFormMapper {

    /**
     * Model -> Form
     */
    @Mapping(target = "password", ignore = true) // パスワードは表示しない
    @Mapping(target = "passwordConfirmation", ignore = true)
    @Mapping(target = "groupIds", source = "model.assignedGroups", qualifiedByName = "groupsToIds")
    @Mapping(target = "isNew", source = "isNew")
    AccountForm toForm(UserAccount model, boolean isNew);

    /**
     * Form -> Model
     */
    @Mapping(target = "assignedGroups", source = "groupIds", qualifiedByName = "idsToGroups")
    @Mapping(target = "createdAt", ignore = true) // Service層で制御
    @Mapping(target = "updatedAt", ignore = true)
    UserAccount toModel(AccountForm form);

    @Named("groupsToIds")
    default List<Integer> groupsToIds(List<UserGroup> groups) {
        if (groups == null)
            return List.of();
        return groups.stream().map(UserGroup::getId).toList();
    }

    @Named("idsToGroups")
    default List<UserGroup> idsToGroups(List<Integer> ids) {
        if (ids == null)
            return List.of();
        // IDのみを持つUserGroupを生成
        return ids.stream().map(id -> {
            UserGroup g = new UserGroup();
            g.setId(id);
            return g;
        }).toList();
    }
}