package me.golf.blog.domain.boardCount.domain.persist;

import lombok.*;
import me.golf.blog.domain.board.domain.persist.Board;

import javax.persistence.*;

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

    private int viewCount = 0;

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
}