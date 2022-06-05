package me.golf.blog.domain.board.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.vo.BoardImage;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateRequest {
    @Valid
    @NotNull(message = "필수 값입니다 - title")
    private Title title;
    @Valid
    @NotNull(message = "필수 값입니다 - content")
    private Content content;
    @Valid
    @NotNull(message = "필수 값입니다 - boardImage")
    private BoardImage boardImage;

    public static BoardCreateRequest of(final Board board) {
        return new BoardCreateRequest(board.getTitle(), board.getContent(), board.getBoardImage());
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .boardImage(boardImage)
                .build();
    }
}
