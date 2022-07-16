package me.golf.blog.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Password;

import javax.validation.Valid;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {
    @JsonProperty("email")
    @Valid
    private Email email;

    @JsonProperty("password")
    @Valid
    private Password password;

    public static LoginRequest of(final Email email, final Password password) {
        return new LoginRequest(email, password);
    }
}
