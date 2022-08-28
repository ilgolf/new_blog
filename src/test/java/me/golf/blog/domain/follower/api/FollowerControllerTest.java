package me.golf.blog.domain.follower.api;

import me.golf.blog.domain.follower.application.FollowerService;
import me.golf.blog.domain.follower.dto.FollowerCreateResponse;
import me.golf.blog.domain.member.domain.vo.RoleType;
import me.golf.blog.domain.member.util.GivenMember;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static me.golf.blog.domain.member.util.GivenMember.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FollowerControllerTest {

    FollowerService followerService;
    FollowerController followerController;

    @BeforeEach
    public void init() {
        followerService = mock(FollowerService.class);
        followerController = new FollowerController(followerService);
    }

    @Test
    @DisplayName("원하는 회원을 팔로우한다.")
    void from() {
        // given
        FollowerCreateResponse response = new FollowerCreateResponse(1L, true);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, GIVEN_EMAIL, RoleType.USER);
        given(followerService.from(anyLong(), anyLong())).willReturn(response);

        // when
        followerController.from(1L, customUserDetails);

        // then
        verify(followerService).from(1L, 1L);
    }
}