package me.golf.blog.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static me.golf.blog.global.config.RedisPolicy.*;

@Configuration
@EnableCaching
public class RedisConfig {

    private final String host;

    private final int port;

    public RedisConfig(@Value("${spring.redis.host}") String host, @Value("${spring.redis.port}") int port) {
        this.host = host;
        this.port = port;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Integer> integerRedisTemplate() {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(this.redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new IntRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer(Charset.defaultCharset()));
        redisTemplate.setHashValueSerializer(new IntRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheConfiguration configuration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(30))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new StringRedisSerializer()));

        Map<String, RedisCacheConfiguration> config = new HashMap<>();

        config.put(MEMBER_KEY, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(MEMBER_TTL)));
        config.put(BOARD_KEY, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(BOARD_TTL)));
        config.put(AUTH_KEY, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(AUTH_TTL)));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory())
                .cacheDefaults(configuration)
                .withInitialCacheConfigurations(config)
                .build();
    }
}
