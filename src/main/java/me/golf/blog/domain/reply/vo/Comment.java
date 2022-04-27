package me.golf.blog.domain.reply.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Comment {
    @NotBlank(message = "필수 값입니다.")
    private String comment;

    public static Comment from(final String comment) {
        return new Comment(comment);
    }

    @JsonValue
    public String comment() {
        return this.comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(comment(), comment.comment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment());
    }
}