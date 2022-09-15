package me.golf.blog.domain.follower.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.follower.domain.persist.Follower;
import me.golf.blog.domain.follower.domain.persist.FollowerRepository;
import me.golf.blog.domain.follower.dto.FollowerAllResponse;
import me.golf.blog.domain.follower.dto.SimpleFollowerResponse;
import me.golf.blog.domain.follower.error.FollowNotFoundException;
import me.golf.blog.domain.follower.error.NotSameFollowerId;
import me.golf.blog.global.common.SliceCustomResponse;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FollowerService {

    private final FollowerRepository followerRepository;

    @Transactional
    public SimpleFollowerResponse follow(final Long fromMemberId, final Long toMemberId) {

        if (Objects.equals(fromMemberId, toMemberId)) {
            throw new NotSameFollowerId(ErrorCode.SAME_ID_DENIED);
        }

        if (existFollower(fromMemberId, toMemberId)) {
            return SimpleFollowerResponse.empty();
        }

        Follower follower = Follower.builder()
                .fromMember(fromMemberId)
                .toMember(toMemberId)
                .build();

        Follower savedFollower = followerRepository.save(follower);

        return new SimpleFollowerResponse(savedFollower.getId(), true);
    }

    @Transactional
    public void cancel(final Long fromMemberId, final Long followId) {

        followerRepository.findByIdAndFromMember(followId, fromMemberId)
                .orElseThrow(() -> new FollowNotFoundException(ErrorCode.FOLLOW_NOT_FOUND))
                .delete();
    }

    @Transactional(readOnly = true)
    public SliceCustomResponse<FollowerAllResponse> getFollowers(final Long memberId, final Pageable pageable) {

        Slice<FollowerAllResponse> followers = followerRepository.findAllWithQuery(memberId, pageable);
        return SliceCustomResponse.of(followers);
    }

    private boolean existFollower(Long fromMemberId, Long toMemberId) {
        return followerRepository.getIdBy(fromMemberId, toMemberId).isPresent();
    }
}
