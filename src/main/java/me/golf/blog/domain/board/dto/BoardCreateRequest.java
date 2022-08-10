package me.golf.blog.domain.board.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.vo.BoardImage;
import me.golf.blog.domain.board.domain.vo.BoardStatus;
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

    public static BoardCreateRequest of(final Board board) {
        return new BoardCreateRequest(board.getTitle(), board.getContent());
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .status(BoardStatus.SAVE)
                .build();
    }
}
