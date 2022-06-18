package me.golf.blog.domain.reply.domain.persist;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.reply.domain.vo.Comment;
import me.golf.blog.global.common.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reply extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id", nullable = false)
    private Long id;

    @Embedded
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "board_id")
    private Board board;

    private Reply(Comment comment, Member member, Board board) {
        this.comment = comment;
        this.member = member;
        this.addBoard(board);
    }

    public static Reply createReply(final Comment comment, final Member member, final Board board) {
        return new Reply(comment, member, board);
    }

    public void addBoard(final Board board) {
        this.board = board;
    }

    public void updateComment(final Comment comment) {
        this.comment = comment;
    }
}