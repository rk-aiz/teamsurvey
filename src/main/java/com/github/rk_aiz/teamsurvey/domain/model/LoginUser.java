package com.github.rk_aiz.teamsurvey.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * ユーザー情報を表すドメインモデル
 */
public class LoginUser extends User {
    private String email;
    private String displayName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 所属グループリスト */
    //@Builder.Default
    private List<UserGroup> assignedGroups = new ArrayList<>();

	/** 
	 * コンストラクタ
	 * 最低限の情報を保持したUserDetails
	 * 実装クラスUserを作成する
	 */
	public LoginUser(
			String username,
			String password,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            boolean enabled,
			Collection<? extends GrantedAuthority> authorities
			) {
		
		super(username, password, enabled, true,
			true, true, authorities);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LoginUser other) {
            return this.getUsername().equals(other.getUsername());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getUsername().hashCode();
    }

    /**
     * 所属グループリストを設定します。
     * ドメインの整合性を保つため、nullが渡された場合は空リストを設定します。
     */
    public void setAssignedGroups(List<UserGroup> assignedGroups) {
        this.assignedGroups = (assignedGroups != null) ? assignedGroups : new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<UserGroup> getAssignedGroups() {
        return assignedGroups;
    }
}