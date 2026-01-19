package com.github.rk_aiz.teamsurvey.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザー情報を表すドメインモデル
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String email;
    private String displayName;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    /** 所属グループリスト */
    @Builder.Default
    private List<UserGroup> assignedGroups = new ArrayList<>();

    /**
     * 所属グループリストを設定します。
     * ドメインの整合性を保つため、nullが渡された場合は空リストを設定します。
     */
    public void setAssignedGroups(List<UserGroup> assignedGroups) {
        this.assignedGroups = (assignedGroups != null) ? assignedGroups : new ArrayList<>();
    }
}