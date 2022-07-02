package me.golf.blog.domain.board.domain.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {
    @Query("select b from Board as b where b.id = :boardId and b.status = 'TEMP' and b.member.id = :memberId")
    Optional<Board> findTempBoardById(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
}