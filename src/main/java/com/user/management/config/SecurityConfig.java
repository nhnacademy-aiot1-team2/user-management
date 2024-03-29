package com.user.management.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정에 대한 클래스입니다.
 * Author : jjunho50
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * BCrypt 암호화를 사용하는 PasswordEncoder Bean을 제공합니다.
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/user/**", "/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs", "/v2/api-docs/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        return http.build();
    }
}