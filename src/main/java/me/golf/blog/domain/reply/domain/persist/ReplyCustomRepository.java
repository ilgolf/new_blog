package me.golf.blog.domain.reply.domain.persist;

import me.golf.blog.domain.reply.dto.ReplyAllResponse;
import me.golf.blog.global.common.PageCustomResponse;
import org.springframework.data.domain.Pageable;

public interface ReplyCustomRepository {
    PageCustomResponse<ReplyAllResponse> findAllWithQuery(final Pageable pageable, final Long boardId);
}
