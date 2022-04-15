package me.golf.blog.global.jwt.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @NotBlank(message = "토큰이 존재하지 않습니다.")
    private String refreshToken;

    @JsonValue
    public String refreshToken() {
        return refreshToken;
    }

    public static RefreshToken from(final String refreshToken) {
        return new RefreshToken(refreshToken);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken refreshToken = (RefreshToken) o;
        return Objects.equals(refreshToken(), refreshToken.refreshToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(refreshToken());
    }
}
