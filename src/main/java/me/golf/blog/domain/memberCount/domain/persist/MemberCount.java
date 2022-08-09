package me.golf.blog.domain.memberCount.domain.persist;

import lombok.*;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.global.common.BaseTimeEntity;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MemberCount {
    private int followerCount = 0;
    private int followingCount = 0;
    private int boardCount = 0;
}