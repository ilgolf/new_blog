package me.golf.blog.domain.board.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.vo.Nickname;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeAllResponse {
    private Long memberId;
    private Nickname nickname;

    public LikeAllResponse(final Member member) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
    }
}
