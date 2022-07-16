package me.golf.blog.domain.board.domain.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public final class Title implements Serializable {

    @Length(min = 6, max = 30)
    @NotBlank(message = "필수 값입니다.")
    @Column(unique = true, length = 40)
    private String title;

    public static Title from(final String title) {
        return new Title(title);
    }

    @JsonValue
    public String title() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title = (Title) o;
        return Objects.equals(title(), title.title());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title());
    }
}