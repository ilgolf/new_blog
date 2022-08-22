package me.golf.blog.domain.member.domain.redisform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MemberRedisRepository {

    private static final String memberKey = "MEMBER";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveMember(final MemberRedisDto memberRedisDto) {
        String memberJson;

        try {
            memberJson = objectMapper.writeValueAsString(memberRedisDto);
        } catch (JsonProcessingException e) {
            throw new MemberNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        Map<String, String> memberMap = new HashMap<>();
        memberMap.put(memberRedisDto.getId(), memberJson);

        redisTemplate.boundHashOps(memberKey).putAll(memberMap);
    }

    public Optional<MemberRedisDto> findDtoById(final Long memberId) {
        String memberJson = (String) redisTemplate.opsForHash().get(memberKey, memberId);

        if (memberJson == null) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(objectMapper.readValue(memberJson, MemberRedisDto.class));
        } catch (JsonProcessingException e) {
            log.error("파싱 실패 : {}", memberJson);
        }

        return Optional.empty();
    }

    public void deleteBy(final Long memberId) {
        redisTemplate.delete(memberId.toString());
    }
}
