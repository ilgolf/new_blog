package me.golf.blog.domain.board.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.domain.persist.BoardCustomRepositoryImpl;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.domain.persist.SearchKeywordRequest;
import me.golf.blog.domain.board.dto.*;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.boardCount.application.BoardCountService;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardReadService {
    private final BoardService boardService;
    private final BoardCountService boardCountService;
    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponse findById(final Long boardId) {
        BoardDTO board = boardService.getBoard(boardId);
        int viewCount = boardCountService.increaseViewCount(board);
        return BoardResponse.of(board, viewCount);
    }

    public List<BoardAllResponse> findAll(final SearchKeywordRequest searchKeyword, final Pageable pageable) {
        return boardRepository.findAllWithQuery(searchKeyword, pageable);
    }

    public List<BoardAllResponse> findByEmail(final Email email, final Pageable pageable) {
        return boardRepository.findByEmail(email, pageable).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    public TempBoardListResponse getTempBoardList(final Long memberId, final Pageable pageable) {
        // todo
        return null;
    }

    public TempDetailResponse getTempBoard(final Long boardId, final Long memberId) {
        // todo
        return null;
    }

    public void deleteTempBoard(final Long boardId, final Long memberId) {
        // todo
    }
}
