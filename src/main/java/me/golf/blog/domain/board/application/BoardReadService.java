package me.golf.blog.domain.board.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.domain.persist.SearchKeywordRequest;
import me.golf.blog.domain.board.domain.vo.BoardStatus;
import me.golf.blog.domain.board.dto.*;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.config.RedisPolicy;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardReadService {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    @Cacheable(key = "#boardId", value = RedisPolicy.BOARD_KEY)
    public BoardResponse findById(final Long boardId) {

        return boardRepository.getBoardDetail(boardId).orElseThrow(() ->
                new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public PageCustomResponse<BoardAllResponse> findAll(final SearchKeywordRequest searchKeyword, final Pageable pageable) {

        return boardRepository.findAllWithQuery(searchKeyword, pageable);
    }

    @Transactional(readOnly = true)
    public PageCustomResponse<BoardAllResponse> getByNickname(final Nickname nickname, final Pageable pageable) {

        return boardRepository.findByNickname(nickname, pageable);
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
