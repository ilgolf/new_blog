package me.golf.blog.domain.member.domain.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Nickname implements Serializable {
    @Column(name = "nickname", nullable = false, unique = true)
    @NotBlank(message = "필수 값입니다. - nickname")
    @Length(max = 15)
    @Pattern(regexp = "^[a-zA-Z]*$", message = "영어만 입력이 가능합니다.")
    private String nickname;

    @JsonValue
    public String nickname() {
        return nickname;
    }

    public static Nickname from(final String nickname) {
        return new Nickname(nickname);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nickname nickname = (Nickname) o;
        return Objects.equals(nickname(), nickname.nickname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname());
    }
}
