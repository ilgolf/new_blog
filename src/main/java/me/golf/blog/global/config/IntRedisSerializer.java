package me.golf.blog.global.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

public class IntRedisSerializer implements RedisSerializer<Integer> {

    @Override
    public byte[] serialize(Integer value) throws SerializationException {
        if (value == null) {
            return new byte[]{};
        }

        return value.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Integer deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }

        return Integer.parseInt(new String(bytes, StandardCharsets.UTF_8));
    }
}
