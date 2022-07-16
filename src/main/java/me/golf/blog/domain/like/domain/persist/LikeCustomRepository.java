package me.golf.blog.domain.like.domain.persist;

import me.golf.blog.domain.board.dto.LikeAllResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LikeCustomRepository {
    Slice<LikeAllResponse> getBoardLikeList(final Long boardId, final Pageable pageable);
}
