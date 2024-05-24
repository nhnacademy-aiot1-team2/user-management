package com.user.management.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis 연결과 관련된 설정을 담당하는 클래스입니다.
 * 캐시를 사용할 수 있도록 @EnableCaching를 통해 캐시를 활성화 합니다.
 * @author parksangwon
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * RedisConnectionFactory를 통해 RedisCacheManager를 설정하고 생성합니다.
     * RedisCacheManager는 캐시 연산에 사용됩니다.
     *
     * @param redisConnectionFactory Redis 연결을 설정하기 위한 인터페이스
     * @return 캐시 작업을 위해 구성된 RedisCacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager.RedisCacheManagerBuilder builder =
                RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory);

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(1L));

        builder.cacheDefaults(configuration);

        return builder.build();
    }
}