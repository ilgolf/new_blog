package me.golf.blog.domain.reply.applicationa;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.board.error.BoardNotFoundException;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.domain.reply.domain.persist.Reply;
import me.golf.blog.domain.reply.domain.persist.ReplyRepository;
import me.golf.blog.domain.reply.domain.vo.Comment;
import me.golf.blog.domain.reply.dto.ReplyAllResponse;
import me.golf.blog.domain.reply.dto.ReplyCreateRequest;
import me.golf.blog.domain.reply.error.ReplyNotFoundException;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public Long create(final ReplyCreateRequest request, final Long boardId, final Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
        Reply reply = replyRepository.save(Reply.createReply(request.getComment(), member, board));

        return reply.getId();
    }

    @Transactional(readOnly = true)
    public PageCustomResponse<ReplyAllResponse> findAll(final Pageable pageable, final Long boardId) {
        return replyRepository.findAllWithQuery(pageable, boardId);
    }

    public void update(final Comment comment, final Long replyId) {
        replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(ErrorCode.REPLY_NOT_FOUND))
                .updateComment(comment);
    }

    public void deleteById(final Long replyId, final Long memberId) {
        Reply reply = replyRepository
                .findByIdAndMemberId(replyId, memberId)
                .orElseThrow(() -> new ReplyNotFoundException(ErrorCode.REPLY_NOT_FOUND))
                .delete();
    }
}
