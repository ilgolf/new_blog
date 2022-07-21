package me.golf.blog.domain.board.domain.redisForm;

import org.springframework.data.repository.CrudRepository;

public interface BoardRedisRepository extends CrudRepository<BoardRedisEntity, Long> {
}
