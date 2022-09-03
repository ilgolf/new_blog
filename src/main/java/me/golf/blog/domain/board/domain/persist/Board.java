package me.golf.blog.domain.board.domain.persist;

import lombok.*;
import me.golf.blog.domain.board.domain.vo.BoardCount;
import me.golf.blog.domain.board.domain.vo.BoardStatus;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.global.common.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Where(clause = "is_deleted = false")
@Table(name = "Board", indexes = {
        @Index(name = "index_title", columnList = "title"),
        @Index(name = "index_idx", columnList = "member_id")
})
public class Board extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @Embedded
    private BoardCount boardCount;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private BoardStatus status;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Builder
    private Board(Long id, Title title, Content content, BoardStatus status, Long memberId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.boardCount = new BoardCount();
        this.status = status;
        this.memberId = memberId;
        this.isDeleted = false;
    }

    public Board addMember(final Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public void updateBoard(final Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
    }

    public void delete() {
        this.isDeleted = true;
        recordDeleteTime();
    }
}