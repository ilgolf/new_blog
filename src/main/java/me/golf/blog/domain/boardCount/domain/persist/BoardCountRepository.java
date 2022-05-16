package me.golf.blog.domain.boardCount.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardCountRepository extends JpaRepository<BoardCount, Long> {
    @Modifying
    @Query("update BoardCount b set b.viewCount = b.viewCount + 1 where b.id = :id")
    int updateView(@Param("id") Long id);
}