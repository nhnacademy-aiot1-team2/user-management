package com.user.management.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 보안 설정을 담당하는 Config 클래스입니다.
 * '/api/user/**' 경로로 오는 모든 요청에 대해서는 인증 과정을 생략합니다.
 *
 * @see WebSecurityConfigurerAdapter WebSecurityConfigurerAdapter에 대한 Javdoc
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 비밀번호를 암호화하는 PasswordEncoder를 Bean으로 등록합니다.
     * BCrypt 방식의 암호화를 사용합니다.
     *
     * @return PasswordEncoder BCrypt 암호화를 사용하는 PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * HTTP 보안 설정을 담당하는 메소드입니다.
     * CSRF를 disable하고, '/api/user/**'로 시작하는 요청은 인증을 생략합니다.
     * 그 외의 모든 요청에 대해서는 인증이 필요하며 기본 HTTP 인증을 사용합니다.
     *
     * @param http HttpSecurity 객체
     * @throws Exception 보안 설정 중 발생할 수 있는 예외
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/user/**", "/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs", "/v2/api-docs/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}