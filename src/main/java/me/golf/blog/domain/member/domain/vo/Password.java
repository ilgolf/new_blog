package me.golf.blog.domain.member.domain.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.member.error.PasswordNullException;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Password {

    @NotBlank(message = "필수 값입니다.")
    @Column(unique = true, nullable = false, length = 120)
    private String password;

    public static Password from(final String password) {
        return new Password(password);
    }

    public static Password encode(final String rawPassword, final PasswordEncoder encoder) {
        validatePassword(rawPassword);
        return new Password(encoder.encode(rawPassword));
    }

    private static void validatePassword(final String rawPassword) {
        if (Objects.isNull(rawPassword) || rawPassword.isBlank()) {
            throw new PasswordNullException(ErrorCode.PASSWORD_NULL_ERROR);
        }
    }

    @JsonValue
    public String password() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(password(), password.password());
    }

    @Override
    public int hashCode() {
        return Objects.hash(password());
    }
}
