package me.golf.blog.domain.board.util;

import me.golf.blog.domain.boardCount.domain.persist.BoardCount;

public class GivenBoardCount {

    public static BoardCount toEntityWithId() {
        return new BoardCount();
    }
}
