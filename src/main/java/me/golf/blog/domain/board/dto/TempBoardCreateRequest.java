package me.golf.blog.domain.board.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.vo.*;

import javax.validation.Valid;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TempBoardCreateRequest {
    @Valid
    private Title title;
    private Content content;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .status(BoardStatus.TEMP)
                .build();
    }
}
