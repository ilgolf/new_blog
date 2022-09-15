package me.golf.blog.domain.follower.api;

import me.golf.blog.domain.follower.application.FollowerService;
import me.golf.blog.domain.follower.dto.FollowerAllResponse;
import me.golf.blog.domain.follower.dto.SimpleFollowerResponse;
import me.golf.blog.domain.member.domain.vo.RoleType;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static me.golf.blog.domain.member.util.GivenMember.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FollowerControllerTest {

    FollowerService followerService;
    FollowerController followerController;
    static CustomUserDetails customUserDetails;

    @BeforeEach
    public void init() {
        followerService = mock(FollowerService.class);
        customUserDetails = new CustomUserDetails(1L, GIVEN_EMAIL, RoleType.USER);
        followerController = new FollowerController(followerService);
    }

    @Test
    @DisplayName("원하는 회원을 팔로우한다.")
    void fromTest() {
        // given
        SimpleFollowerResponse response = new SimpleFollowerResponse(1L, true);
        given(followerService.follow(anyLong(), anyLong())).willReturn(response);

        // when
        boolean result = followerController.follow(1L, customUserDetails).getBody().isResult();

        // then
        assertThat(result).isTrue();
        verify(followerService).follow(1L, 1L);
    }

    @Test
    @DisplayName("팔로우한 회원을 조회한다.")
    void getFollowersTest() {
        // given
        FollowerAllResponse response = new FollowerAllResponse(1L, 1L, GIVEN_NICKNAME,
                LocalDateTime.now());

        // when
        followerController.getFollowers(PageRequest.of(0, 7), customUserDetails);

        // then
        verify(followerService).getFollowers(anyLong(), any());
    }

    @Test
    @DisplayName("회원 팔로우를 취소한다.")
    void cancelTest() {
        // given

        // when
        followerController.cancel(1L, customUserDetails);

        // then
        verify(followerService).cancel(1L, 1L);
    }
}