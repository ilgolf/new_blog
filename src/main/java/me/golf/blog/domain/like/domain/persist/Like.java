package me.golf.blog.domain.like.domain.persist;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.global.common.BaseTimeEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "deleted = false")
@Table(name = "likes")
public class Like extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id", nullable = false)
    private Long id;

    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Like(Member member, Board board) {
        this.member = member;
        this.board = board;
    }

    public static Like createLike(final Member member, final Board board) {
        return new Like(member, board);
    }

    public Like delete() {
        this.deleted = true;
        recordDeleteTime();
        return this;
    }
}