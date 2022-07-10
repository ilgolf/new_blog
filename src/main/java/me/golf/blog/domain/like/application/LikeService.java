package me.golf.blog.domain.like.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.like.domain.persist.Like;
import me.golf.blog.domain.like.domain.persist.LikeRepository;
import me.golf.blog.domain.like.error.LikeNotFoundException;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Long likeBoard(final Long boardId, final Long memberId) {
        // todo
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

        board.getBoardCount().plusLike();

        return likeRepository.save(Like.createLike(member, board)).getId();
    }

    public void unLikeBoard(final Long likeId) {
        // todo
        likeRepository.findById(likeId)
                .orElseThrow(() -> new LikeNotFoundException(ErrorCode.LIKE_NOT_FOUND))
                .delete();
    }
}
