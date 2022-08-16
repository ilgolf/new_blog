package me.golf.blog.domain.member.domain.redisform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRedisRepository {

    private static final String memberKey = "MEMBER";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveMember(final MemberRedisDto memberRedisDto) throws JsonProcessingException {
        String memberJson = objectMapper.writeValueAsString(memberRedisDto);
        redisTemplate.opsForHash().put(memberKey, memberRedisDto.getId(), memberJson);
    }

    public Optional<MemberRedisDto> findDtoById(final Long memberId) {
        return Optional.ofNullable((MemberRedisDto) redisTemplate.opsForHash().get(memberKey, memberId.toString()));
    }
}
