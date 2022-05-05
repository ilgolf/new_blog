package me.golf.blog.domain.member.domain.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Name {
    @Column(nullable = false, length = 13)
    @NotBlank(message = "필수 값입니다.")
    private String name;

    public static Name from(final String name) {
        return new Name(name);
    }

    @JsonValue
    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(name(), name.name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name());
    }
}
