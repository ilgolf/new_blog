package me.golf.blog.domain.follower.domain.persist;

import me.golf.blog.domain.follower.dto.FollowerAllResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FollowerCustomRepository {

    Slice<FollowerAllResponse> findAllWithQuery(final Long memberId, final Pageable pageable);
}
