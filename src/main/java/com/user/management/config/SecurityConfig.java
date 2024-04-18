package com.user.management.config;

import com.user.management.service.impl.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정에 대한 클래스입니다.
 *
 * @author jjunho50
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2UserService oAuth2UserService;

    /**
     * HttpSecurity 설정을 이용해 SecurityFilterChain Bean을 제공합니다.
     * Controller 와 Swagger 에서 제공하는 모든 요청을 허용합니다.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 예외 처리
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .anyRequest().permitAll() //나머지 uri는 모든 접근 허용
                .and().oauth2Login()
                .userInfoEndpoint()//로그인 완료 후 회원 정보 받기
                .userService(oAuth2UserService).and().and().build(); //
    }
}