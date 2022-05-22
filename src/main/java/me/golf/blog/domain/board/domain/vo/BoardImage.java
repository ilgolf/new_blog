package me.golf.blog.domain.board.domain.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public final class BoardImage {
    @NotBlank(message = "필수 값입니다. - boardImage")
    @Column(name = "board_image")
    private String boardImage;

    @JsonValue
    public String boardImage() {
        return boardImage;
    }

    public static BoardImage from(final String boardImage) {
        return new BoardImage(boardImage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardImage boardImage = (BoardImage) o;
        return Objects.equals(boardImage(), boardImage.boardImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardImage());
    }
}