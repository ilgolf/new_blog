package me.golf.blog.domain.board.domain.persist;

import me.golf.blog.domain.board.domain.vo.BoardStatus;
import me.golf.blog.domain.board.domain.vo.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Board> findById(final Long id);
    Optional<Board> findByIdAndStatusAndMemberId(final Long id, final BoardStatus status, final Long memberId);
    boolean existsByTitle(final Title title);
}