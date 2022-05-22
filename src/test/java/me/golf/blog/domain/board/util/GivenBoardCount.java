package me.golf.blog.domain.board.util;

import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.boardCount.domain.persist.BoardCount;

public class GivenBoardCount {

    public static BoardCount toEntityWithId(final Board board) {
        return BoardCount.builder()
                .id(1L)
                .viewCount(0)
                .likeCount(0)
                .build();
    }
}
