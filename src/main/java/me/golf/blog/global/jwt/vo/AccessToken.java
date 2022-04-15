package me.golf.blog.global.jwt.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken {

    @NotBlank(message = "토큰이 존재하지 않습니다.")
    private String accessToken;

    @JsonValue
    public String accessToken() {
        return accessToken;
    }

    public static AccessToken from(final String accessToken) {
        return new AccessToken(accessToken);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessToken accessToken = (AccessToken) o;
        return Objects.equals(accessToken(), accessToken.accessToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken());
    }
}
