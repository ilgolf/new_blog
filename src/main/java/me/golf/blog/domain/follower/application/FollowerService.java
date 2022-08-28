package me.golf.blog.domain.follower.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.follower.domain.persist.Follower;
import me.golf.blog.domain.follower.domain.persist.FollowerRepository;
import me.golf.blog.domain.follower.dto.FollowerAllResponse;
import me.golf.blog.domain.follower.dto.FollowerCreateResponse;
import me.golf.blog.domain.follower.error.FollowNotFoundException;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.common.SliceCustomResponse;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowerService {

    private final FollowerRepository followerRepository;

    public FollowerCreateResponse from(final Long fromMemberId, final Long toMemberId) {
        Follower follower = Follower.builder()
                .fromMember(fromMemberId)
                .toMember(toMemberId)
                .build();

        Follower savedFollower = followerRepository.save(follower);

        return new FollowerCreateResponse(savedFollower.getId(), true);
    }

    public void cancel(final Long fromMemberId, final Long followId) {
        followerRepository.findByIdAndFromMember(followId, fromMemberId)
                .orElseThrow(() -> new FollowNotFoundException(ErrorCode.FOLLOW_NOT_FOUND))
                .delete();
    }

    public SliceCustomResponse<FollowerAllResponse> getFollowers(final Long memberId, final Pageable pageable) {
        Slice<FollowerAllResponse> followers = followerRepository.findAllWithQuery(memberId, pageable);

        return SliceCustomResponse.of(followers);
    }
}
