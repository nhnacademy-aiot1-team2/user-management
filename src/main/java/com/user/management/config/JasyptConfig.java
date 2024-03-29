package com.user.management.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jasypt 암호화 설정을 위한 Configuration 클래스입니다.
 * application.properties 에 저장된 DB.username, DB.password, DB.url 을 암호화 합니다.
 * Author : jjunho50
 */
@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

    /**
     * application.properties 에 암호화를 위한 비밀번호 값 (실제 DB 비밀번호 x)
     */
    @Value("${jasypt.encryptor.password}")
    private String password;

    /**
     * BeanName : jasyptStringEncryptor
     * Jasypt 의 PooledPBEStringEncryptor 클래스를 사용하여 구성하며, 해당 빈은 암호화 및 복호화 작업에 사용됩니다.
     *
     * @return 암호화 및 복호화에 사용될 수 있는 StringEncryptor 객체.
     */
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
