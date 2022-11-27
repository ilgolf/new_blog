package me.golf.blog.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Password;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    @JsonProperty("email")
    @Valid
    @NotNull(message = "필수 값입니다.")
    private Email email;

    @JsonProperty("password")
    @Valid
    @NotNull(message = "필수 값입니다.")
    private Password password;

    public static LoginRequest of(final Email email, final Password password) {
        return new LoginRequest(email, password);
    }
}
