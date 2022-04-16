package me.golf.blog.domain.board.domain.persist;

import lombok.*;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.global.common.BaseTimeEntity;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "Board", indexes = {
        @Index(name = "index_title", columnList = "title", unique = true)
})
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Board(Title title, Content content, int view) {
        this.title = title;
        this.content = content;
        this.view = view;
    }

    public void addMember(final Member member) {
        this.member = member;
    }

    public static Board of(final Title title, final Content content, final int view) {
        return new Board(title, content, view);
    }

    public void updateBoard(final Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
    }
}