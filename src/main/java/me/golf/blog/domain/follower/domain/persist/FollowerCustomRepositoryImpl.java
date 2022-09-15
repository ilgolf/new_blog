package me.golf.blog.domain.follower.domain.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.follower.dto.FollowerAllResponse;
import me.golf.blog.global.common.SliceCustomResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static me.golf.blog.domain.follower.domain.persist.QFollower.*;
import static me.golf.blog.domain.member.domain.persist.QMember.*;

@Repository
@RequiredArgsConstructor
public class FollowerCustomRepositoryImpl implements FollowerCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Slice<FollowerAllResponse> findAllWithQuery(final Long memberId, final Pageable pageable) {
        List<FollowerAllResponse> followers = query.select(Projections.constructor(FollowerAllResponse.class,
                        follower.id,
                        member.id,
                        member.nickname,
                        follower.createTime))
                .from(follower)
                .innerJoin(member).on(member.id.eq(follower.toMember))
                .where(follower.fromMember.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        if (followers.size() == pageable.getPageSize() + 1) {
            followers.remove(followers.size() - 1);
            return new SliceImpl<>(followers, pageable, true);
        }

        return new SliceImpl<>(followers, pageable, false);
    }

    @Override
    public Optional<Long> getIdBy(Long fromMemberId, Long toMemberId) {
        return Optional.ofNullable(
                query.select(follower.id)
                        .from(follower)
                        .where(follower.toMember.eq(toMemberId)
                                .and(follower.fromMember.eq(fromMemberId)))
                        .limit(1)
                        .fetchOne()
        );
    }
}
