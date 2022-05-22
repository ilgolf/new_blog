package me.golf.blog.domain.board.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.domain.persist.BoardQueryRepository;
import me.golf.blog.domain.board.domain.persist.SearchKeywordRequest;
import me.golf.blog.domain.board.dto.BoardAllResponse;
import me.golf.blog.domain.board.dto.BoardDTO;
import me.golf.blog.domain.board.dto.BoardResponse;
import me.golf.blog.domain.boardCount.application.BoardCountService;
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
    private final BoardQueryRepository boardQueryRepository;

    public BoardResponse findById(final Long boardId) {
        BoardDTO board = boardService.getBoard(boardId);
        int viewCount = boardCountService.increaseViewCount(board);
        return BoardResponse.of(board, viewCount);
    }

    public List<BoardAllResponse> findAll(final SearchKeywordRequest searchKeyword, final Pageable pageable) {
        return boardQueryRepository.findAll(searchKeyword, pageable);
    }
}
