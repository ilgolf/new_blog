package me.golf.blog.domain.board.util;

import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.vo.BoardCount;
import me.golf.blog.domain.board.domain.vo.BoardStatus;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;

public class GivenBoard {
    public static Title GIVEN_TITLE = Title.from("테스트용 게시판 제목");
    public static Content GIVEN_CONTENT = Content.from("테스트 게시판 내용 입니다. 테스트 테스트");

    public static Board toEntity() {
        return Board.builder()
                .title(GIVEN_TITLE)
                .content(GIVEN_CONTENT)
                .status(BoardStatus.SAVE)
                .build();
    }

    public static Board toEntityWithBoardCount(final BoardCount boardCount) {
        return Board.builder()
                .id(1L)
                .title(GIVEN_TITLE)
                .status(BoardStatus.SAVE)
                .content(GIVEN_CONTENT)
                .build();
    }

    public static Board toTempBoard() {
        return Board.builder()
                .id(1L)
                .title(GIVEN_TITLE)
                .status(BoardStatus.TEMP)
                .content(GIVEN_CONTENT)
                .build();
    }
}
