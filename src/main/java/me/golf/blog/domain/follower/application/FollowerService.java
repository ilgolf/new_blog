package me.golf.blog.domain.follower.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.follower.domain.persist.Follower;
import me.golf.blog.domain.follower.domain.persist.FollowerRepository;
import me.golf.blog.domain.follower.dto.FollowerCreateResponse;
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
}
