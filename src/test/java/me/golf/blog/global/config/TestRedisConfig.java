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

    public static final String MAX_MEMORY_REDIS = "maxmemory 128M";
    private final RedisServer redisServer;
    private final int port;

    public TestRedisConfig(@Value("${spring.redis.port}") int redisPort) throws IOException {

        port = redisPort;
        redisServer = RedisServer.builder()
                .port(isRedisRunning() ? findAvailablePort() : this.port)
                .setting(MAX_MEMORY_REDIS)
                .build();

        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }

    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(port));
    }

    public int findAvailablePort() throws IOException {

        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception ignore) {
        }

        return StringUtils.hasText(pidInfo.toString());
    }
}
