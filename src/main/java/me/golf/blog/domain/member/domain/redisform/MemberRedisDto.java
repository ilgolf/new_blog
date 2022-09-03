package me.golf.blog.domain.member.domain.redisform;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.member.domain.persist.Member;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberRedisDto {
    private String id;
    private String email;
    private String name;
    private String nickname;
    private LocalDate birth;
    private int followerCount;
    private int followingCount;
    private int boardCount;

    public static MemberRedisDto of(final Member member) {
        return new MemberRedisDto(
                member.getId().toString(),
                member.getEmail().email(),
                member.getName().name(),
                member.getNickname().nickname(),
                member.getBirth(),
                member.getMemberCount().getFollowerCount(),
                member.getMemberCount().getFollowingCount(),
                member.getMemberCount().getBoardCount());
    }
}
