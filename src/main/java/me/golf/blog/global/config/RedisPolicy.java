package me.golf.blog.global.config;

public class RedisPolicy {
    public static final String MEMBER_KEY = "M_KEY";
    public static final String BOARD_KEY = "B_KEY";
    public static final String AUTH_KEY = "A_KEY";

    public static final int MEMBER_TTL = 30;
    public static final int BOARD_TTL = 30;
    public static final int AUTH_TTL = 180;
}
