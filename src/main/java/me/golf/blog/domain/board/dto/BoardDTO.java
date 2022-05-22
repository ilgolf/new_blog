package me.golf.blog.domain.board.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.vo.BoardImage;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardDTO {
    private Title title;
    private Content content;
    private BoardImage boardImage;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private Long boardCountId;

    public static BoardDTO of(final Board board) {
        return new BoardDTO(board.getTitle(), board.getContent(), board.getBoardImage(), board.getLastModifiedTime(), board.getCreatedBy(),
                board.getBoardCount().getId());
    }
}
