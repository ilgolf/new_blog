package me.golf.blog.domain.reply.applicationa;

import me.golf.blog.domain.board.application.BoardService;
import me.golf.blog.domain.board.util.GivenBoard;
import me.golf.blog.domain.member.application.MemberService;
import me.golf.blog.domain.member.util.GivenMember;
import me.golf.blog.domain.reply.dto.ReplyUpdateRequest;
import me.golf.blog.domain.reply.domain.persist.Reply;
import me.golf.blog.domain.reply.domain.persist.ReplyRepository;
import me.golf.blog.domain.reply.domain.vo.Comment;
import me.golf.blog.domain.reply.dto.ReplyAllResponse;
import me.golf.blog.domain.reply.dto.ReplyCreateRequest;
import me.golf.blog.domain.reply.error.ReplyNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReplyServiceTest {
    @Autowired MemberService memberService;
    @Autowired BoardService boardService;
    @Autowired ReplyService replyService;
    @Autowired ReplyRepository replyRepository;

    static Long memberId;
    static Long boardId;
    static Long replyId;

    @BeforeEach
    void init() {
        memberId = memberService.create(GivenMember.toEntity()).getMemberId();
        boardId = boardService.create(GivenBoard.toEntity(), memberId);
        replyId = replyService.create(new ReplyCreateRequest(Comment.from("안녕하세요, 테스트입니다. 반가워요")), boardId, memberId);
    }

    @Test
    @DisplayName("정상적으로 댓글 전체를 조회한다.")
    void findAllTest() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        for (int i = 0; i < 10; i++) {
            Comment comment = Comment.from("안녕하세요, 테스트입니다. 반가워요" + (i + 1));
            replyService.create(new ReplyCreateRequest(comment), boardId, memberId);
        }

        // when
        List<ReplyAllResponse> replies = replyService.findAll(pageable, boardId).getData();

        // then
        assertThat(replies.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("댓글을 수정할 수 있다.")
    void updateTest() {
        // given
        ReplyUpdateRequest request = new ReplyUpdateRequest(Comment.from("안녕하세요, 테스트입니다. 반가워요 수정본"));

        // when
        replyService.update(request.getComment(), replyId);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(ErrorCode.REPLY_NOT_FOUND));

        // then
        assertThat(reply.getComment().comment()).isEqualTo("안녕하세요, 테스트입니다. 반가워요 수정본");
        assertThat(reply.getComment().comment()).isNotEqualTo("안녕하세요, 테스트입니다. 반가워요");
    }

    @Test
    @DisplayName("댓글을 삭제할 수 있다.")
    void deleteTest() {
        // when
        replyService.deleteById(replyId, memberId);

        // then
        assertThat(replyRepository.findById(replyId).orElseThrow(
                () -> new ReplyNotFoundException(ErrorCode.REPLY_NOT_FOUND)).isDeleted()).isEqualTo(true);
    }
}