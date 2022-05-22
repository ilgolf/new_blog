package me.golf.blog.domain.board.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.vo.BoardImage;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponse {
    private Title title;
    private Content content;
    private BoardImage boardImage;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private int view;

    public static BoardResponse of(final BoardDTO board, int view) {
        return new BoardResponse(board.getTitle(), board.getContent(), board.getBoardImage(),
                board.getLastModifiedAt(), board.getCreatedBy(), view);
    }
}
