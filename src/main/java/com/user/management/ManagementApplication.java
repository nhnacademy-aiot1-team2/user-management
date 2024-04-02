package com.user.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>{@link EnableScheduling} 어노테이션은 스프링의 스케쥴러를 활성화시킵니다.
 * (마지막 로그인 시간이 1달이 지난 유저는 자동으로 휴면 상태로 전환됩니다.)
 *
 * <p>{@link EnableDiscoveryClient}
 * 서비스 디스커버리 서버에 자신의 정보를 등록하고, 다른 서비스의 정보를 불러올 수 있게 됩니다.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class ManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class, args);
    }
}