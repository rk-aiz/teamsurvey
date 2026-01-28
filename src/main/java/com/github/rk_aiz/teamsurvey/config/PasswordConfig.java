package com.github.rk_aiz.teamsurvey.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
	
	@Bean
	public PasswordEncoder passwordEncode() {
		
		// エンコーダーの設定
		//return NoOpPasswordEncoder.getInstance();
		
		return new BCryptPasswordEncoder();
	}
}
