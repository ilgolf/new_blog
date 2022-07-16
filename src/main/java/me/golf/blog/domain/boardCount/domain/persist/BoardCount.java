package me.golf.blog.domain.boardCount.domain.persist;

import lombok.*;
import me.golf.blog.domain.board.domain.persist.Board;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@Table(name = "board_count")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BoardCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_count_id", nullable = false, updatable = false)
    private Long id;
    private int viewCount = 0;
    private int likeCount = 0;

    public BoardCount plusView() {
        this.viewCount ++;
        return this;
    }

    public BoardCount plusLike() {
        this.likeCount ++;
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