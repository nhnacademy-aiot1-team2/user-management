package com.user.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 이 클래스는 Swagger 설정을 정의합니다.
 * Swagger를 사용하여 API의 문서화를 쉽게 진행하고 API를 시각적으로 확인할 수 있습니다.
 *
 * @author jjunho50
 * @version 1.0.0
 * @Configuration : Configuration 클래스임을 명시
 * @EnableWebMvc : Spring MVC를 활성화하여 컨트롤러를 통해 웹 기능을 수행
 */
@Configuration
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

    /**
     * Swagger에서 사용할 Docket 객체를 생성합니다.
     * Docket : API 문서 설정을 정의하고 관리하는 기본 구조체
     *
     * @return Docket 객체를 반환합니다.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.user.management.controller"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(apiInfoMetaData());
    }

    /**
     * ApiInfo : API 의 기본 정보를 담는 클래스
     *
     * @return ApiInfo 객체를 반환합니다.
     */
    private ApiInfo apiInfoMetaData() {
        return new ApiInfoBuilder().title("user-management api")
                .description("유저 관리 API 명세서입니다.")
                .license("Apache 2.0")
                .version("1.0.0")
                .build();
    }
}