package me.golf.blog.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.vo.BoardImage;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardDTO implements Serializable {
    public static final long serialVersionUID = 2L;

    @JsonProperty("title")
    private Title title;

    @JsonProperty("content")
    private Content content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    private LocalDateTime lastModifiedAt;

    private String createdBy;

    private Long boardCountId;

    public static BoardDTO of(final Board board) {
        return new BoardDTO(board.getTitle(), board.getContent(), board.getLastModifiedTime(),
                board.getCreatedBy(), board.getBoardCount().getId());
    }
}
