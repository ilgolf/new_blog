package me.golf.blog.domain.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.global.jwt.vo.AccessToken;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessTokenResponse {
    private AccessToken accessToken;

    public static AccessTokenResponse from(final AccessToken accessToken) {
        return new AccessTokenResponse(accessToken);
    }
}
