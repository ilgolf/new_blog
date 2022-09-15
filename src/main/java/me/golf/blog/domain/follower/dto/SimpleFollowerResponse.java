package me.golf.blog.domain.follower.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SimpleFollowerResponse {

    private Long followId;
    private boolean result;

    public static SimpleFollowerResponse empty() {
        return new SimpleFollowerResponse();
    }
}
