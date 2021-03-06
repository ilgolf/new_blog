package me.golf.blog.domain.board.application;

import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.domain.persist.SearchKeywordRequest;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.board.dto.*;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.member.application.MemberService;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.dto.JoinResponse;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.domain.member.util.GivenMember;
import me.golf.blog.global.error.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static me.golf.blog.domain.board.util.GivenBoard.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardServiceTest {
    @Autowired BoardService boardService;
    @Autowired BoardRepository boardRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;
    @Autowired BoardReadService boardReadService;

    static Member member;
    static Long boardId;

    @BeforeEach
    void init() {
        JoinResponse joinResponse = memberService.create(GivenMember.toEntity());
        
        member = memberRepository.findById(joinResponse.getMemberId()).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

        boardId = boardService.create(toEntity(), member.getId());
    }

    @Test
    @DisplayName("boardId??? ????????? ????????? ?????? ??????")
    @Transactional(readOnly = true)
    void findById() {
        BoardResponse boardResponse = boardReadService.findById(boardId);

        assertThat(boardResponse.getTitle()).isEqualTo(GIVEN_TITLE);
        assertThat(boardResponse.getContent()).isEqualTo(GIVEN_CONTENT);
        assertThat(boardResponse.getView()).isEqualTo(1);
    }

    @Test
    @DisplayName("????????????????????? ???????????? ???????????? 10??? ???????????????.")
    @Transactional(readOnly = true)
    void findAll() {
        // given
        for (int i = 0; i < 10; i++) {
            Board board = Board.builder()
                    .title(Title.from("????????? ????????????. " + i))
                    .content(Content.from("???????????? Content ?????????. ??????????????????"))
                    .build();
            boardService.create(board, member.getId());
        }

        SearchKeywordRequest keyword = SearchKeywordRequest.builder()
                .title(null)
                .content(null)
                .nickname(null)
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<BoardAllResponse> boards = boardReadService.findAll(keyword, pageable).getData();

        assertThat(boards.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ?????? ????????? ???????????? ????????????.")
    void update() {
        // given
        BoardUpdateRequest request =
                BoardUpdateRequest.of(Title.from("????????? ????????? ???????????????."), Content.from("??????????????? ????????? ????????? ???????????????."));

        // when
        boardService.update(request.toEntity(), boardId, member.getId());
        Board board = boardRepository.findById(boardId).orElseThrow
                (() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        // then
        assertThat(board.getTitle()).isEqualTo(Title.from("????????? ????????? ???????????????."));
        assertThat(board.getContent()).isEqualTo(Content.from("??????????????? ????????? ????????? ???????????????."));
        assertThat(board.getLastModifiedTime()).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("?????? ????????? ???????????? ???????????????.")
    void findByEmail() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<BoardAllResponse> responses = boardReadService.findByEmail(GivenMember.GIVEN_EMAIL, pageable).getData();

        // then
        assertThat(responses.get(0).getTitle()).isEqualTo(GIVEN_TITLE);
        assertThat(responses.size()).isEqualTo(1);
    }


    @Test
    @DisplayName("????????? ????????? ????????????.")
    void delete() {
        boardService.delete(boardId, member.getId());

        assertThat(boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND)).isDeleted()).isEqualTo(true);
    }

    @Test
    @DisplayName("???????????? ?????? ????????????.")
    void createTemp() {
        // given
        TempBoardCreateRequest request = getTempBoardCreateRequest();

        // when
        Long tempBoard = boardService.createTemp(request.toEntity(), member.getId());

        // then
        Title title = boardRepository.findTempBoardById(tempBoard, member.getId()).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND)).getTitle();

        assertThat(title.title()).isEqualTo("?????? ????????? ????????? ??????????????????.");
    }

    @Test
    @DisplayName("?????? ????????? ????????? ????????? ????????????.")
    void tempBoardList() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Board board = getTempBoardCreateRequest().toEntity();
        boardService.createTemp(board, member.getId());

        // when
        List<TempBoardListResponse> tempBoardList =
                boardReadService.getTempBoardList(member.getId(), pageable).getData();

        // then
        assertThat(tempBoardList.size()).isEqualTo(1);
    }

    private TempBoardCreateRequest getTempBoardCreateRequest() {
        return new TempBoardCreateRequest(
                Title.from("?????? ????????? ????????? ??????????????????."), null);
    }
}