package me.golf.blog.domain.boardCount.domain.persist;

import lombok.*;
import me.golf.blog.domain.board.domain.persist.Board;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@Table(name = "board_count")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_count_id", nullable = false, updatable = false)
    private Long id;

    @Builder.Default
    private int viewCount = 0;

    @Builder.Default
    private int likeCount = 0;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "board_id")
    private Board board;

    public static BoardCount createBoardCount(final Board board) {
        BoardCount boardCount = new BoardCount();
        boardCount.addBoard(board);
        return boardCount;
    }

    public void addBoard(final Board board) {
        this.board = board;
        board.addBoardCount(this);
    }

    public synchronized BoardCount plusView() {
        this.viewCount ++;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardCount boardCount = (BoardCount) o;
        return Objects.equals(id, boardCount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}