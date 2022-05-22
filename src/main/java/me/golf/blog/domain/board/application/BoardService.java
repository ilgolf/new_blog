package me.golf.blog.domain.board.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.dto.BoardAllResponse;
import me.golf.blog.domain.board.dto.BoardDTO;
import me.golf.blog.domain.board.dto.BoardResponse;
import me.golf.blog.domain.board.error.BoardMissMatchException;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.boardCount.domain.persist.BoardCount;
import me.golf.blog.domain.boardCount.domain.persist.BoardCountRepository;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardCountRepository boardCountRepository;

    public Long create(final Board board, final Long memberId) {
        Member member = getMember(memberId);
        board.addMember(member);
        Board savedBoard = boardRepository.save(board);
        BoardCount boardCount = BoardCount.createBoardCount(savedBoard);
        boardCountRepository.save(boardCount);
        return savedBoard.getId();
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#boardId", value = "getBoard")
    public BoardDTO getBoard(final Long boardId) {
        return boardRepository.findById(boardId)
                .map(BoardDTO::of)
                .orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    public void update(final Board updateBoard, final Long boardId, final Long memberId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        if (!Objects.equals(board.getMember().getId(), memberId)) {
            throw new BoardMissMatchException(ErrorCode.BOARD_MISS_MATCH);
        }
        board.updateBoard(updateBoard);
    }

    public void delete(final Long boardsId, final Long memberId) {
        Board board = boardRepository.findById(boardsId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        if (!Objects.equals(board.getMember().getId(), memberId)) {
            throw new BoardMissMatchException(ErrorCode.BOARD_MISS_MATCH);
        }

        boardRepository.delete(board);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
