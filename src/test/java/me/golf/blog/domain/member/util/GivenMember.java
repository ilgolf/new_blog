package me.golf.blog.domain.member.util;

import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.vo.RoleType;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.domain.vo.Password;
import me.golf.blog.domain.memberCount.domain.persist.MemberCount;

import java.time.LocalDate;

public class GivenMember {
    public static final Email GIVEN_EMAIL = Email.from("ilgolc@naver.com");
    public static final Password GIVEN_PASSWORD = Password.from("123456789");
    public static final Nickname GIVEN_NICKNAME = Nickname.from("ssar");
    public static final Name GIVEN_NAME = Name.from("kim3");
    public static final LocalDate GIVEN_BIRTH = LocalDate.of(1999, 10, 25);

    public static Member toEntity() {
        return Member.builder()
                .id(1L)
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .nickname(GIVEN_NICKNAME)
                .name(GIVEN_NAME)
                .role(RoleType.USER)
                .birth(GIVEN_BIRTH)
                .build();
    }

    public static Member toEntityWithCount() {
        return Member.builder()
                .id(1L)
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .nickname(GIVEN_NICKNAME)
                .name(GIVEN_NAME)
                .role(RoleType.USER)
                .memberCount(new MemberCount())
                .birth(GIVEN_BIRTH)
                .build();
    }
}
