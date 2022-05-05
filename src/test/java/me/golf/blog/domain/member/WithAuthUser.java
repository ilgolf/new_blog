package me.golf.blog.domain.member;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.LocalDate;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthUserSecurityContextFactory.class)
public @interface WithAuthUser {
    long id() default 1L;
    String email() default "ilgolc@naver.com";
    String password() default "123456789";
    String name() default "kim3";
    String nickname() default "ssar";
}
