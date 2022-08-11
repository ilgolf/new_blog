package me.golf.blog.domain.board.domain.persist;

import me.golf.blog.domain.board.domain.vo.BoardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {

    Optional<Board> findByIdAndStatusAndMemberId(final Long id, final BoardStatus status, final Long memberId);
}