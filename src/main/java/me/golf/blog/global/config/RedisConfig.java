package me.golf.blog.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static me.golf.blog.global.config.RedisPolicy.*;

@Configuration
@EnableCaching
public class RedisConfig {

    private final String host;

    private final int port;

    private final ObjectMapper objectMapper;

    public RedisConfig(@Value("${spring.redis.host}") String host, @Value("${spring.redis.port}") int port,
                       ObjectMapper objectMapper) {
        this.host = host;
        this.port = port;
        this.objectMapper = objectMapper;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheConfiguration configuration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(30))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(objectMapper)));

        Map<String, RedisCacheConfiguration> config = new HashMap<>();

        config.put(MEMBER_POLICY.keyValue, configuration.entryTtl(Duration.ofMinutes(MEMBER_POLICY.ttlMinutes)));
        config.put(BOARD_POLICY.keyValue, configuration.entryTtl(Duration.ofMinutes(BOARD_POLICY.ttlMinutes)));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory())
                .withInitialCacheConfigurations(config)
                .build();
    }
}
