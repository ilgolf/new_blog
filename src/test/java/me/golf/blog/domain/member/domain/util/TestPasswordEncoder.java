package me.golf.blog.domain.member.domain.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestPasswordEncoder implements PasswordEncoder {

    private static final int LOG_ROUNDS = 4;

    @Override
    public String encode(CharSequence rawPassword) {
        return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(LOG_ROUNDS));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }

    public static PasswordEncoder initialize() {
        return new TestPasswordEncoder();
    }
}
