package me.golf.blog.domain.board.util;

import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.vo.BoardImage;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.boardCount.domain.persist.BoardCount;
import me.golf.blog.domain.member.domain.persist.Member;

public class GivenBoard {
    public static Title GIVEN_TITLE = Title.from("테스트용 게시판 제목");
    public static Content GIVEN_CONTENT = Content.from("테스트 게시판 내용 입니다. 테스트 테스트");
    public static BoardImage BOARD_IMAGE = BoardImage.from("/test/user/image.png");

    public static Board toEntity() {
        return Board.builder()
                .title(GIVEN_TITLE)
                .content(GIVEN_CONTENT)
                .boardImage(BOARD_IMAGE)
                .build();
    }

    public static Board toEntityWithBoardCount(final BoardCount boardCount) {
        return Board.builder()
                .title(GIVEN_TITLE)
                .content(GIVEN_CONTENT)
                .boardImage(BOARD_IMAGE)
                .boardCount(boardCount)
                .build();
    }
}
