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

    public void save(final BoardRedisDto board) throws JsonProcessingException {
        String boardJson = objectMapper.writeValueAsString(board);
        redisTemplate.opsForValue().set(board.getId(), boardJson);
    }

    public Optional<BoardRedisDto> findById(final Long boardId) throws JsonProcessingException {
        String boardJson = redisTemplate.opsForValue().get(boardId.toString());
        return Optional.ofNullable(objectMapper.readValue(boardJson, BoardRedisDto.class));
    }

    public void delete(final BoardRedisDto board) {
        redisTemplate.delete(board.getId());
    }
}
