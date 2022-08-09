package me.golf.blog.domain.like.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.application.BoardReadService;
import me.golf.blog.domain.board.application.BoardService;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.dto.LikeAllResponse;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.like.domain.persist.Like;
import me.golf.blog.domain.like.domain.persist.LikeRepository;
import me.golf.blog.domain.like.error.LikeNotFoundException;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.global.common.SliceCustomResponse;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardReadService boardReadService;

    public Long likeBoard(final Long boardId, final Long memberId) {
        // todo
        Board board = boardReadService.getBoardOne(boardId);
        board.getBoardCount().plusLike();

        return likeRepository.save(Like.createLike(memberId, boardId)).getId();
    }

    public void unLikeBoard(final Long likeId) {
        // todo
        likeRepository.findById(likeId)
                .orElseThrow(() -> new LikeNotFoundException(ErrorCode.LIKE_NOT_FOUND))
                .delete();
    }

    @Transactional(readOnly = true)
    public SliceCustomResponse<LikeAllResponse> getBoardLikeList(final Long boardId, final Pageable pageable) {
        // todo
        return SliceCustomResponse.of(likeRepository.getBoardLikeList(boardId, pageable));
    }
}
