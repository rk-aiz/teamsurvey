package com.github.rk_aiz.teamsurvey.application.form;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.type.Authority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupForm {

    private Integer groupId;

    @NotBlank(message = "グループ名は必須です")
    @Size(max = 50, message = "グループ名は{max}文字以内で入力してください")
    private String groupName;

    @NotNull(message = "権限を指定してください")
    private Authority authority;

    public UserGroup toModel() {
        UserGroup group = new UserGroup();
        BeanUtils.copyProperties(this, group);
        // フォームに存在しないプロパティ（isSystemGroupなど）はデフォルト値または別途設定
        return group;
    }
}
