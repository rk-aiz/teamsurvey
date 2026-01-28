package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.LoginUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUserDatailsServiceImpl implements UserDetailsService {

	/** DI */
	private final LoginUserRepository loginUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		LoginUser loginUser =
				loginUserRepository.findByUsername(username);
		
		if (loginUser == null) {
			throw new UsernameNotFoundException(
					username + " => 指定しているユーザー名は存在しません"
				);
		}
		
		//対象データがあれば、UserDetailsの実装クラスを返す
		return loginUser;
	}
}
