package me.golf.blog.domain.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.global.jwt.vo.AccessToken;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleAuthResponse {

    private AccessToken accessToken;
    private boolean result;

    public static SimpleAuthResponse from(final AccessToken accessToken, final boolean result) {
        return new SimpleAuthResponse(accessToken, result);
    }
}
