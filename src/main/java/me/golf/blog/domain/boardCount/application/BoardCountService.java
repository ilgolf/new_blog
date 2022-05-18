package me.golf.blog.domain.boardCount.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.dto.BoardDTO;
import me.golf.blog.domain.boardCount.domain.persist.BoardCountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardCountService {
    private final BoardCountRepository boardCountRepository;

    public int increaseViewCount(final BoardDTO board) {
        return boardCountRepository.updateView(board.getBoardCountId());
    }
}
