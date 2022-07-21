package me.golf.blog.domain.board.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.domain.persist.SearchKeywordRequest;
import me.golf.blog.domain.board.domain.redisForm.BoardRedisEntity;
import me.golf.blog.domain.board.domain.redisForm.BoardRedisRepository;
import me.golf.blog.domain.board.dto.*;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.boardCount.application.BoardCountService;
import me.golf.blog.domain.like.application.LikeService;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.common.SliceCustomResponse;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardReadService {
    private final BoardCountService boardCountService;
    private final BoardRepository boardRepository;
    private final BoardRedisRepository boardRedisRepository;
    private final LikeService likeService;

    @Transactional
    public BoardResponse findById(final Long boardId) {
        BoardRedisEntity boardRedisEntity = boardRedisRepository.findById(boardId)
                .orElseGet(() -> boardRepository.findRedisEntity(boardId).orElseThrow(
                        () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND)));

        int viewCount = boardCountService.increaseViewCount(boardRedisEntity.getBoardCountId());

        return BoardResponse.of(boardRedisEntity, viewCount);
    }

    public PageCustomResponse<BoardAllResponse> findAll(final SearchKeywordRequest searchKeyword, final Pageable pageable) {
        return boardRepository.findAllWithQuery(searchKeyword, pageable);
    }

    public PageCustomResponse<BoardAllResponse> findByEmail(final Email email, final Pageable pageable) {
        return boardRepository.findByEmail(email, pageable);
    }

    // 회원이 소유한 모든 임시 게시물 조회
    public PageCustomResponse<TempBoardListResponse> getTempBoardList(final Long memberId, final Pageable pageable) {
        // todo
        return boardRepository.findAllTempBoard(memberId, pageable);
    }

    public TempDetailResponse getTempBoard(final Long boardId, final Long memberId) {
        // todo
        Board board = boardRepository.findTempBoardById(boardId, memberId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        return new TempDetailResponse(board.getTitle(), board.getContent());
    }

    public SliceCustomResponse<LikeAllResponse> getBoardLikeList(final Long boardId, final Pageable pageable) {
        // todo
        return SliceCustomResponse.of(likeService.getBoardLikeMembers(boardId, pageable));
    }
}
