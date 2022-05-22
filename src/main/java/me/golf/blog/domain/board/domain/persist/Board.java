package me.golf.blog.domain.board.domain.persist;

import lombok.*;
import me.golf.blog.domain.board.domain.vo.BoardImage;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.boardCount.domain.persist.BoardCount;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.global.common.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "Board", indexes = {
        @Index(name = "index_title", columnList = "title")
})
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @OneToOne(mappedBy = "board", fetch = FetchType.EAGER)
    private BoardCount boardCount;

    @Embedded
    private BoardImage boardImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Board(final Title title, final Content content, final Member member) {
        this.title = title;
        this.content = content;
        addMember(member);
    }

    public Board addMember(final Member member) {
        this.member = member;
        member.addBoard(this);
        return this;
    }

    public static Board of(final Title title, final Content content, final Member member) {
        return new Board(title, content, member);
    }

    public void updateBoard(final Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
    }

    public void addBoardCount(final BoardCount boardCount) {
        this.boardCount = boardCount;
    }
}