package me.golf.blog.domain.board.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {
    @Query("select b from Board as b where b.id = :boardId and b.status = 'TEMP' and b.memberId = :memberId")
    Optional<Board> findTempBoardById(@Param("boardId") final Long boardId, @Param("memberId") final Long memberId);

    @Query("select b from Board as b where b.id = :boardId")
    Optional<Board> findWithMemberById(@Param("boardId") final Long boardId);
}