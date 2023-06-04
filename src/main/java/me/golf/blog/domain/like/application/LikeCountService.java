package me.golf.blog.domain.like.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.golf.blog.domain.board.application.BoardReadService;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.like.domain.persist.Like;
import me.golf.blog.domain.like.domain.persist.LikeRepository;
import me.golf.blog.domain.like.error.LikeNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeCountService {

    private final BoardReadService boardReadService;
    private final LikeRepository likeRepository;
    private final TransactionTemplate transactionTemplate;

    @Async("likeCountIncreaseExecutor")
    public void increaseLikeCount(final Long boardId, final Long likeId) {

        Board boardResult = transactionTemplate.execute(s -> {
            Board board = boardReadService.getBoardOne(boardId);

            try {
                board.getBoardCount().plusLike();
            } catch (Exception e) {
                log.error("좋아요 count가 이상해요 : {}", e.getMessage());
                s.setRollbackOnly();
                throw new RuntimeException(e);
            }

            return board;
        });

        if (boardResult == null) {
            likeRepository.deleteById(likeId);
        }
    }

    @Async("likeCountDecreaseExecutor")
    public void decreaseLikeCount(final Long boardId, final Long likeId) {

        Like like = likeRepository.findById(likeId).orElseThrow(
                () -> new LikeNotFoundException(ErrorCode.LIKE_NOT_FOUND));

        Board boardResult = transactionTemplate.execute(s -> {
            Board board = boardReadService.getBoardOne(boardId);

            try {
                board.getBoardCount().minusLike();
            } catch (Exception e) {
                s.setRollbackOnly();
                throw new RuntimeException(e);
            }

            return board;
        });

        if (boardResult == null) {
            like.delete();
        }
    }
}
