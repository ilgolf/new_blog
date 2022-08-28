package me.golf.blog.domain.follower.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.member.domain.vo.Nickname;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowerAllResponse {

    private Long followerId;

    private Long memberId;

    private Nickname nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createAt;

    public FollowerAllResponse(Long followerId, Long memberId, Nickname nickname, LocalDateTime createAt) {
        this.followerId = followerId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.createAt = createAt.toLocalDate();
    }
}
