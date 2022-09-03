package me.golf.blog.domain.reply.domain.persist;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.reply.domain.vo.Comment;
import me.golf.blog.global.common.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "is_deleted = false")
public class Reply extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id", nullable = false)
    private Long id;

    @Embedded
    private Comment comment;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "board_id")
    private Long boardId;

    private Reply(Comment comment, Long memberId, Long boardId) {
        this.comment = comment;
        this.memberId = memberId;
        this.boardId = boardId;
    }

    public static Reply createReply(final Comment comment, final Long memberId, final Long boardId) {
        return new Reply(comment, memberId, boardId);
    }

    public Reply delete() {
        this.isDeleted = true;
        this.recordDeleteTime();
        return this;
    }

    public void updateComment(final Comment comment) {
        this.comment = comment;
    }
}