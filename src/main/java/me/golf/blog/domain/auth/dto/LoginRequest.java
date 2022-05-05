package me.golf.blog.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Password;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {
    @JsonProperty("email")
    private Email email;

    @JsonProperty("password")
    private Password password;

    public static LoginRequest of(final Email email, final Password password) {
        return new LoginRequest(email, password);
    }
}
