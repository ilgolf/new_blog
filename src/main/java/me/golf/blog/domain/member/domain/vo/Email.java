package me.golf.blog.domain.member.domain.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Email implements Serializable {
    @javax.validation.constraints.Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "필수 값입니다. - email")
    @Column(unique = true, nullable = false)
    private String email;

    public static Email from(final String email) {
        return new Email(email);
    }

    @JsonValue
    public String email() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(email(), email.email());
    }

    @Override
    public int hashCode() {
        return Objects.hash(email());
    }
}
