package me.golf.blog.domain.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.redisform.MemberRedisDto;
import me.golf.blog.domain.memberCount.domain.persist.MemberCount;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {
    private Email email;
    private Name name;
    private Nickname nickname;
    private int age;
    private int followerCount;
    private int followingCount;
    private int boardCount;

    public static MemberResponse of(final MemberRedisDto member) {
        int memberYear = member.getBirth().getYear();
        int now = LocalDate.now().getYear();
        int age = now - memberYear;

        return new MemberResponse(Email.from(member.getEmail()),
                Name.from(member.getName()),
                Nickname.from(member.getNickname()),
                age,
                member.getFollowerCount(),
                member.getFollowingCount(), member.getBoardCount());
    }
}
