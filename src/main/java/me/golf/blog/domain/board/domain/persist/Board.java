package me.golf.blog.domain.board.domain.persist;

import lombok.*;
import me.golf.blog.domain.board.domain.vo.BoardStatus;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.boardCount.domain.persist.BoardCount;
import me.golf.blog.domain.like.domain.persist.Like;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.global.common.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@Where(clause = "is_deleted = false")
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_count_id")
    private BoardCount boardCount;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private BoardStatus status = BoardStatus.SAVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;

    public void updateBoard(final Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
    }

    public void delete() {
        this.isDeleted = true;
        recordDeleteTime();
    }

    public Board addMember(final Member member) {
        this.member = member;
        return this;
    }

    public void addBoardCount(final BoardCount boardCount) {
        this.boardCount = boardCount;
    }
}