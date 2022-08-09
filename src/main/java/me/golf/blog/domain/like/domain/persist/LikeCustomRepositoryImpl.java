package me.golf.blog.domain.like.domain.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.dto.LikeAllResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static me.golf.blog.domain.like.domain.persist.QLike.like;
import static me.golf.blog.domain.member.domain.persist.QMember.member;

@Repository
@RequiredArgsConstructor
public class LikeCustomRepositoryImpl implements LikeCustomRepository {
    private final JPAQueryFactory query;

    @Override
    public Slice<LikeAllResponse> getBoardLikeList(Long boardId, Pageable pageable) {
        List<LikeAllResponse> likes = query.select(Projections.constructor(LikeAllResponse.class,
                        member.id,
                        member.nickname)
                )
                .from(like)
                .innerJoin(member).on(member.id.eq(like.memberId))
                .where(like.boardId.eq(boardId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        if (likes.size() == pageable.getPageSize() + 1) {
            likes.remove(likes.size() - 1);
            return new SliceImpl<>(likes, pageable, true);
        }

        return new SliceImpl<>(likes, pageable, false);
    }
}
