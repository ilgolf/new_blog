package me.golf.blog.domain.board.domain.persist;

import me.golf.blog.domain.board.domain.vo.BoardStatus;
import me.golf.blog.domain.board.domain.vo.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name="javax.persistence.lock.timeout", value = "3000")})
    Optional<Board> findByIdAndMemberId(final Long id, final Long memberId);
    Optional<Board> findByIdAndStatusAndMemberId(final Long id, final BoardStatus status, final Long memberId);
    boolean existsByTitle(final Title title);
}