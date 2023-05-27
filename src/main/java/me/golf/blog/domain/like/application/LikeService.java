package me.golf.blog.domain.like.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.dto.LikeAllResponse;
import me.golf.blog.domain.like.domain.persist.Like;
import me.golf.blog.domain.like.domain.persist.LikeRepository;
import me.golf.blog.domain.like.error.LikeNotFoundException;
import me.golf.blog.global.common.SliceCustomResponse;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeCountService likeCountService;

    @Transactional
    public Long likeBoard(final Long boardId, final Long memberId) {
        Long likeId = likeRepository.save(Like.createLike(memberId, boardId)).getId();
        likeCountService.increaseLikeCount(boardId, memberId, likeId);
        return likeId;
    }

    @Transactional
    public void unLikeBoard(final Long likeId) {
        likeRepository.findById(likeId)
                .orElseThrow(() -> new LikeNotFoundException(ErrorCode.LIKE_NOT_FOUND))
                .delete();
    }

    @Transactional(readOnly = true)
    public SliceCustomResponse<LikeAllResponse> getBoardLikeList(final Long boardId, final Pageable pageable) {
        return SliceCustomResponse.of(likeRepository.getBoardLikeList(boardId, pageable));
    }
}
