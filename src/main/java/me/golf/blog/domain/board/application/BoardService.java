package me.golf.blog.domain.board.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.domain.redisForm.BoardRedisEntity;
import me.golf.blog.domain.board.domain.redisForm.BoardRedisRepository;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.board.error.BoardMissMatchException;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.board.error.TitleDuplicationException;
import me.golf.blog.domain.boardCount.application.BoardCountService;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.domain.memberCount.application.MemberCountService;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardCountService boardCountService;
    private final MemberCountService memberCountService;
    private final BoardRedisRepository boardRedisRepository;

    @Transactional
    public Long create(final Board board, final Long memberId) {
        existTitle(board.getTitle());
        Board savedBoard = boardRepository.save(board.addMember(getMember(memberId)));
        board.addBoardCount(boardCountService.saveBoardCount());
        memberCountService.increaseBoardCount(getMember(memberId));
        boardRedisRepository.save(new BoardRedisEntity(savedBoard));
        return savedBoard.getId();
    }

    @Transactional
    public void update(final Board updateBoard, final Long boardId, final Long memberId) {
        Board board = getBoardEntity(boardId);

        if (!Objects.equals(board.getMember().getId(), memberId)) {
            throw new BoardMissMatchException(ErrorCode.BOARD_MISS_MATCH);
        }

        if (board.getTitle().equals(updateBoard.getTitle())) {
            existTitle(updateBoard.getTitle());
        }

        board.updateBoard(updateBoard);
        boardRedisRepository.save(new BoardRedisEntity(board));
    }

    @Transactional
    public Long createTemp(final Board board, final Long memberId) {
        board.addMember(getMember(memberId));
        return boardRepository.save(board).getId();
    }

    @Transactional
    public void delete(final Long boardsId, final Long memberId) {
        Board board = getBoardEntity(boardsId);

        if (!Objects.equals(board.getMember().getId(), memberId)) {
            throw new BoardMissMatchException(ErrorCode.BOARD_MISS_MATCH);
        }

        board.delete();
    }

    @Transactional
    public void deleteTempBoard(final Long boardId, final Long memberId) {
        // todo
        Board board = boardRepository.findTempBoardById(boardId, memberId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        board.delete();
    }

    private Board getBoardEntity(Long boardsId) {
        return boardRepository.findWithMemberById(boardsId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    private void existTitle(final Title title) {
        if (boardRepository.existByTitle(title).isPresent()) {
            throw new TitleDuplicationException(ErrorCode.DUPLICATE_TITLE);
        }
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
