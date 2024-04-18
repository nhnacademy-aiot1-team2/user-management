package com.user.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 일반적인 설정을 정의하기 위한 class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
public class Config {
    /**
     * BCrypt 암호화를 사용하는 PasswordEncoder Bean을 제공합니다.
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
