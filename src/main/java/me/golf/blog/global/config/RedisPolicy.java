package me.golf.blog.global.config;

public enum RedisPolicy {

    MEMBER_POLICY("M_KEY", 30),
    BOARD_POLICY("B_KEY", 30);

    final String keyValue;
    final long ttlMinutes;

    RedisPolicy(String keyValue, long ttlMinutes) {
        this.keyValue = keyValue;
        this.ttlMinutes = ttlMinutes;
    }
}
