package me.golf.blog.domain.board.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.domain.persist.SearchKeywordRequest;
import me.golf.blog.domain.board.domain.redisForm.BoardRedisDto;
import me.golf.blog.domain.board.domain.redisForm.BoardRedisRepository;
import me.golf.blog.domain.board.domain.vo.BoardStatus;
import me.golf.blog.domain.board.dto.*;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardReadService {
    private final BoardRepository boardRepository;
    private final BoardRedisRepository boardRedisRepository;

    @Transactional
    public BoardResponse findById(final Long boardId) throws JsonProcessingException {
        BoardRedisDto boardRedisDto = boardRedisRepository.findById(boardId).orElseGet(() -> {
                    Board board = boardRepository.findById(boardId).orElseThrow(
                            () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

                    BoardRedisDto boardRedis = new BoardRedisDto(board);

                    try {
                        boardRedisRepository.save(boardRedis);
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException("파싱 실패!");
                    }

                    return boardRedis;
        });

        int viewCount = boardRepository.increaseViewCount(boardId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        return BoardResponse.of(boardRedisDto, viewCount);
    }

    @Transactional(readOnly = true)
    public PageCustomResponse<BoardAllResponse> findAll(final SearchKeywordRequest searchKeyword, final Pageable pageable) {
        return boardRepository.findAllWithQuery(searchKeyword, pageable);
    }

    @Transactional(readOnly = true)
    public PageCustomResponse<BoardAllResponse> findByEmail(final Email email, final Pageable pageable) {
        return boardRepository.findByEmail(email, pageable);
    }

    @Transactional(readOnly = true)
    public PageCustomResponse<TempBoardListResponse> getTempBoardList(final Long memberId, final Pageable pageable) {
        return boardRepository.findAllTempBoard(memberId, pageable);
    }

    @Transactional(readOnly = true)
    public TempDetailResponse getTempBoard(final Long boardId, final Long memberId) {
        Board board = boardRepository.findByIdAndStatusAndMemberId(boardId, BoardStatus.TEMP, memberId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        return new TempDetailResponse(board.getTitle(), board.getContent());
    }

    public Board getBoardOne(final Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }
}
