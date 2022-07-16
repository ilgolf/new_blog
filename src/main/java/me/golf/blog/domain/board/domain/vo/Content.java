package me.golf.blog.domain.board.domain.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public final class Content implements Serializable {
    @NotBlank(message = "필수 값입니다.")
    @Length(min = 20, max = 500)
    @Column(columnDefinition = "TEXT")
    private String content;

    public static Content from(final String content) {
        return new Content(content);
    }

    @JsonValue
    public String content() {
        return this.content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(content(), content.content());
    }

    @Override
    public int hashCode() {
        return Objects.hash(content());
    }
}