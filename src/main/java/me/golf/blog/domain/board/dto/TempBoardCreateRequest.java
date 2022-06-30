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

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TempBoardCreateRequest {
    @Valid
    private Title title;
    private Content content;
    private BoardImage boardImage;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .boardImage(boardImage)
                .build();
    }
}
