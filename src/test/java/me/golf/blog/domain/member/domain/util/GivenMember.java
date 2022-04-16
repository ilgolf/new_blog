package me.golf.blog.domain.member.domain.util;

import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.RoleType;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.domain.vo.Password;

import java.time.LocalDate;

public class GivenMember {

    public static final Email EMAIL = Email.from("golf@naver.com");
    public static final Password PASSWORD = Password.from("asdf12345");
    public static final Name NAME = Name.from("노경태");
    public static final Nickname NICKNAME = Nickname.from("kim");
    public static final LocalDate BIRTH = LocalDate.of(1996, 10, 25);

    public static Member toEntity() {
        return Member.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name(NAME)
                .role(RoleType.USER)
                .nickname(NICKNAME)
                .birth(BIRTH)
                .build();
    }
}
