package me.golf.blog.global.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.global.jwt.vo.AccessToken;
import me.golf.blog.global.jwt.vo.RefreshToken;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDTO {

    @JsonProperty("refreshToken")
    private AccessToken accessToken;

    @JsonProperty("accessToken")
    private RefreshToken refreshToken;

    public static TokenDTO of(final AccessToken accessToken, final RefreshToken refreshToken) {
        return new TokenDTO(accessToken, refreshToken);
    }
}