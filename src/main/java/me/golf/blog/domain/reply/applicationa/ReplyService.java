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
public class ReplyService {
    private final ReplyRepository replyRepository;

    @Transactional
    public Long create(final ReplyCreateRequest request, final Long boardId, final Long memberId) {
        return replyRepository.save(Reply.createReply(request.getComment(), memberId, boardId)).getId();
    }

    @Transactional(readOnly = true)
    public PageCustomResponse<ReplyAllResponse> findAll(final Pageable pageable, final Long boardId) {
        return replyRepository.findAllWithQuery(pageable, boardId);
    }

    @Transactional
    public void update(final Comment comment, final Long replyId) {
        replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(ErrorCode.REPLY_NOT_FOUND))
                .updateComment(comment);
    }

    @Transactional
    public void deleteById(final Long replyId, final Long memberId) {
        replyRepository
                .findByIdAndMemberId(replyId, memberId)
                .orElseThrow(() -> new ReplyNotFoundException(ErrorCode.REPLY_NOT_FOUND))
                .delete();
    }
}
