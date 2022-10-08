package me.golf.blog.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@ActiveProfiles("test")
@Configuration
public class TestRedisConfig {

    public static final String MAX_MEMORY_REDIS = "maxmemory 256M";
    private final RedisServer redisServer;

    public TestRedisConfig(@Value("${spring.redis.port}") int redisPort) {

        redisServer = RedisServer.builder()
                .port(redisPort)
                .setting(MAX_MEMORY_REDIS)
                .build();
        try {
            redisServer.start();
        } catch (Exception ignore) {
        }
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
