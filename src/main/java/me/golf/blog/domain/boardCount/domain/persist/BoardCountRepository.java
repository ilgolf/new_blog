package me.golf.blog.domain.boardCount.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCountRepository extends JpaRepository<BoardCount, Long> {
}