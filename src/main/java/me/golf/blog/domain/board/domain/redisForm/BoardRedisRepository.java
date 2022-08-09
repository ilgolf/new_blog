package me.golf.blog.domain.board.domain.redisForm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void save(final BoardRedisEntity board) throws JsonProcessingException {
        String boardJson = objectMapper.writeValueAsString(board);
        redisTemplate.opsForValue().set(board.getId(), boardJson);
    }

    public Optional<BoardRedisEntity> findById(final Long boardId) throws JsonProcessingException {
        String boardJson = redisTemplate.opsForValue().get(boardId.toString());
        return Optional.ofNullable(objectMapper.readValue(boardJson, BoardRedisEntity.class));
    }

    public void delete(final BoardRedisEntity board) {
        redisTemplate.delete(board.getId());
    }
}
