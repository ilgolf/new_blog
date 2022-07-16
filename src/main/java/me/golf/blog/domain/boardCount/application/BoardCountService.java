package me.golf.blog.domain.boardCount.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.golf.blog.domain.boardCount.domain.persist.BoardCount;
import me.golf.blog.domain.boardCount.domain.persist.BoardCountRepository;
import me.golf.blog.domain.member.error.MemberCountNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardCountService {
    private final BoardCountRepository boardCountRepository;

    public BoardCount saveBoardCount() {
        BoardCount boardCount = new BoardCount();
        return boardCountRepository.save(boardCount);
    }

    @Transactional
    public int increaseViewCount(final Long boardCountId) {
        return boardCountRepository.findById(boardCountId)
                .orElseThrow(() -> new MemberCountNotFoundException(ErrorCode.USER_COUNT_NOT_FOUND))
                .plusView() // 더티 체킹 발생!
                .getViewCount(); // 1차 영속성 캐시에서 조회
    }
}
