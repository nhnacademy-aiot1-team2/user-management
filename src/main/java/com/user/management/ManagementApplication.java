package com.user.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 사용자 관리 어플리케이션의 시작점입니다.
 *
 * <p>이 클래스는 Spring Boot 어플리케이션의 메인 클래스입니다.
 *
 * <p>SpringApplicationBuilder를 사용하여 어플리케이션을 구성하고 실행합니다.
 *
 * <p>SpringBoot 어플리케이션은 본 클래스를 통해 시작됩니다. {@link SpringApplication#run(String...)} 메서드는
 * Spring application context를 구동시킵니다.
 *
 * <p>{@link EnableScheduling} 어노테이션은 스프링의 스케쥴러를 활성화시킵니다. 이 어노테이션으로 인해 {@code @Scheduled} 어노테이션이
 * 붙은 메서드가 주기적으로 실행됩니다. (마지막 로그인 시간이 1달이 지난 유저는 자동으로 휴면 상태로 전환됩니다.)
 *
 * <p>{@link EnableDiscoveryClient} 어노테이션은 서비스 발견 기능을 활성화시킵니다. 이 어노테이션으로 인해 이 애플리케이션이 서비스 디스커버리
 * 서버에 자신의 정보를 등록하고, 다른 서비스의 정보를 불러올 수 있게 됩니다.
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class ManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class, args);
    }
}