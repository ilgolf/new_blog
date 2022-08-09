package me.golf.blog.domain.board.util;

import me.golf.blog.domain.board.domain.vo.BoardCount;

public class GivenBoardCount {

    public static BoardCount toEntityWithId() {
        return new BoardCount();
    }
}
