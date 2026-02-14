package com.github.rk_aiz.teamsurvey.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) {

        http
                // ★HTTPリクエストに対するセキュリティ設定
                .authorizeHttpRequests(authz -> authz
                        // 「/auth(ログイン画面), favicon.svg」へのアクセスは認証を必要としない
                        .requestMatchers("/auth", "/setup", "/favicon.svg").permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                        .permitAll()
                        // Actuatorの保護: healthは公開、それ以外はADMIN権限が必要
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/actuator/**").hasAuthority("ADMIN")
                        // 【管理者権限設定】 url : /admin/**は管理者しかアクセスできない
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        // ★その他のリクエストはすべて認証が必要
                        .anyRequest().authenticated())

                // ★フォームによるログイン設定
                .formLogin(form -> form
                        // ログイン画面のURL
                        .loginPage("/auth")
                        // ログイン処理のURLを指定
                        .loginProcessingUrl("/authentication")
                        // ユーザー名のname属性を指定
                        .usernameParameter("usernameInput")
                        // パスワードのname属性を指定
                        .passwordParameter("passwordInput")
                        // ログイン成功時のURLを指定
                        .defaultSuccessUrl("/", true)
                        // ログイン失敗時のURLを指定
                        .failureUrl("/auth?error"))

                // ★ログアウト設定
                .logout(logout -> logout
                        // ログアウトを処理するURLを指定
                        .logoutUrl("/logout")
                        // ログアウト成功時のリダイレクト先を指定
                        .logoutSuccessUrl("/auth?logout")
                        // ログアウト時にセッションを無効にする
                        .invalidateHttpSession(true)
                        // ログアウト時にCookieを削除する
                        .deleteCookies("JSESSIONID"));

        return http.build();
    }
}