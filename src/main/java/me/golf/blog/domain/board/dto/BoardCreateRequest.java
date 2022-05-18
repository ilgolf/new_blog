package me.golf.blog.domain.board.dto;

import lombok.Getter;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;

@Getter
public class BoardCreateRequest {
    private Title title;
    private Content content;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .build();
    }
}
