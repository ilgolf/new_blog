package me.golf.blog.domain.board.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.domain.vo.BoardStatus;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.board.error.BoardMissMatchException;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.board.error.TitleDuplicationException;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.global.config.RedisPolicy;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long create(final Board board, final Long memberId) {

        existTitle(board.getTitle());
        Board savedBoard = boardRepository.save(board.addMember(memberId));
        memberRepository.increaseBoardCount(memberId);
        return savedBoard.getId();
    }

    @Transactional
    @CachePut(key = "#boardId", value = RedisPolicy.BOARD_KEY)
    public void update(final Board updateBoard,
                       final Long boardId, final Long memberId) {
        Board board = getBoardEntity(boardId);

        if (!Objects.equals(board.getMemberId(), memberId)) {
            throw new BoardMissMatchException(ErrorCode.BOARD_MISS_MATCH);
        }

        if (board.getTitle().equals(updateBoard.getTitle())) {
            existTitle(updateBoard.getTitle());
        }

        board.updateBoard(updateBoard);
    }

    @Transactional
    public Long createTemp(final Board board, final Long memberId) {
        board.addMember(memberId);
        return boardRepository.save(board).getId();
    }

    @Transactional
    @CacheEvict(key = "#boardId", value = RedisPolicy.BOARD_KEY)
    public void delete(final Long boardId, final Long memberId) {
        Board board = getBoardEntity(boardId);

        if (!Objects.equals(board.getMemberId(), memberId)) {
            throw new BoardMissMatchException(ErrorCode.BOARD_MISS_MATCH);
        }

        board.delete();
    }

    @Transactional
    public void deleteTempBoard(final Long boardId, final Long memberId) {
        Board board = boardRepository.findByIdAndStatusAndMemberId(boardId, BoardStatus.TEMP, memberId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        board.delete();
    }

    private Board getBoardEntity(final Long boardsId) {
        return boardRepository.findById(boardsId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    private void existTitle(final Title title) {
        if (boardRepository.existsByTitle(title)) {
            throw new TitleDuplicationException(ErrorCode.DUPLICATE_TITLE);
        }
    }
}
