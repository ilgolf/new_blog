package me.golf.blog.domain.board.application;

import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.board.dto.BoardAllResponse;
import me.golf.blog.domain.board.dto.BoardResponse;
import me.golf.blog.domain.board.dto.BoardUpdateRequest;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.member.WithAuthUser;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.util.GivenMember;
import me.golf.blog.global.error.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static me.golf.blog.domain.board.util.GivenBoard.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardReadService boardReadService;

    static Member member;
    static Long boardId;

    @BeforeEach
    void init() {
        member = memberRepository.save(GivenMember.toEntity());
        boardId = boardService.create(toEntity(), member.getId());
    }

    @Test
    @DisplayName("boardId로 원하는 게시판 상세 조회")
    @WithAuthUser
    @Transactional(readOnly = true)
    void findById() {
        BoardResponse boardResponse = boardReadService.findById(boardId);

        assertThat(boardResponse.getTitle()).isEqualTo(GIVEN_TITLE);
        assertThat(boardResponse.getContent()).isEqualTo(GIVEN_CONTENT);
        assertThat(boardResponse.getView()).isEqualTo(1);
    }

    @Test
    @DisplayName("데이터베이스에 존재하는 게시판을 10개 조회해온다.")
    @Transactional(readOnly = true)
    void findAll() {
        for (int i = 0; i < 10; i++) {
            Board board = Board.builder()
                    .title(Title.from("테스트 용입니다. " + i))
                    .content(Content.from("테스트용 Content 입니다. 재미있다아앙"))
                    .build();
            boardService.create(board, member.getId());
        }

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        List<BoardAllResponse> boards = boardService.findAll(pageable);

        assertThat(boards.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("게시판 정보를 수정하고 수정 날짜와 수정자를 기록한다.")
    @WithAuthUser
    @Transactional
    void update() {
        // given
        BoardUpdateRequest request =
                BoardUpdateRequest.of(Title.from("수정된 게시판 제목입니다."), Content.from("안녕하세요 수정된 게시판 내용입니다."));

        // when
        boardService.update(request.toEntity(), boardId, member.getId());
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        // then
        assertThat(board.getTitle()).isEqualTo(Title.from("수정된 게시판 제목입니다."));
        assertThat(board.getContent()).isEqualTo(Content.from("안녕하세요 수정된 게시판 내용입니다."));
        assertThat(board.getLastModifiedBy()).isEqualTo(1L);
        assertThat(board.getLastModifiedTime()).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("게시판 정보를 삭제한다.")
    @WithAuthUser
    @Transactional
    void delete() {
        boardService.delete(1L, member.getId());
        assertThrows(BoardNotFoundException.class, () -> boardService.getBoard(1L));
    }
}