package me.golf.blog.domain.reply.domain.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.reply.dto.ReplyAllResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static me.golf.blog.domain.reply.domain.persist.QReply.*;

@Repository
@RequiredArgsConstructor
public class ReplyCustomRepositoryImpl implements ReplyCustomRepository {
    private final JPAQueryFactory query;

    public List<ReplyAllResponse> findAllWithQuery(final Pageable pageable, final Long boardId) {
        return query.select(Projections.constructor(ReplyAllResponse.class,
                reply.createdBy,
                reply.comment,
                reply.createTime))
                .from(reply)
                .where(reply.board.id.eq(boardId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
