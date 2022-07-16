package me.golf.blog.domain.reply.domain.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.reply.dto.ReplyAllResponse;
import me.golf.blog.global.common.PageCustomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static me.golf.blog.domain.reply.domain.persist.QReply.*;

@Repository
@RequiredArgsConstructor
public class ReplyCustomRepositoryImpl implements ReplyCustomRepository {
    private final JPAQueryFactory query;

    public PageCustomResponse<ReplyAllResponse> findAllWithQuery(final Pageable pageable, final Long boardId) {
        List<ReplyAllResponse> replies = query.select(Projections.constructor(ReplyAllResponse.class,
                        reply.createdBy,
                        reply.comment,
                        reply.createTime))
                .from(reply)
                .where(reply.board.id.eq(boardId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (replies.size() == 0) {
            return PageCustomResponse.of(Page.empty());
        }

        JPAQuery<Reply> count = query.select(reply)
                .from(reply);

        return PageCustomResponse.of(PageableExecutionUtils.getPage(replies, pageable, () -> count.fetch().size()));
    }
}
