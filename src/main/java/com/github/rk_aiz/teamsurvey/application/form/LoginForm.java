package com.github.rk_aiz.teamsurvey.application.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
	
	/** ユーザー名 */
	private String usernameInput;
	
	/** パスワード */
	private String passwordInput;
}