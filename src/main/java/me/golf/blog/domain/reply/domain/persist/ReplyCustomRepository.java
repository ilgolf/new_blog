package me.golf.blog.domain.reply.domain.persist;

import me.golf.blog.domain.reply.dto.ReplyAllResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReplyCustomRepository {
    List<ReplyAllResponse> findAllWithQuery(final Pageable pageable, final Long boardId);
}
