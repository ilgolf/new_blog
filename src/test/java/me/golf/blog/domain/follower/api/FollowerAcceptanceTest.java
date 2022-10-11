package me.golf.blog.domain.follower.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.golf.blog.domain.follower.application.FollowerService;
import me.golf.blog.domain.follower.dto.FollowerAllResponse;
import me.golf.blog.domain.follower.dto.SimpleFollowerResponse;
import me.golf.blog.domain.member.WithAuthUser;
import me.golf.blog.domain.member.util.GivenMember;
import me.golf.blog.global.common.SliceCustomResponse;
import me.golf.blog.global.config.AbstractContainerBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@SpringBootTest
class FollowerAcceptanceTest extends AbstractContainerBaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    FollowerService followerService;

    @Test
    @DisplayName("회원을 팔로우 한다.")
    @WithAuthUser
    void followTest() throws Exception {

        //given
        SimpleFollowerResponse response = new SimpleFollowerResponse(1L, true);
        when(followerService.follow(anyLong(), anyLong())).thenReturn(response);

        // when
        mockMvc.perform(post("/api/v1/followers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("follower/follow", responseFields(
                        fieldWithPath("followId").description("팔로우 고유 식별자"),
                        fieldWithPath("result").description("응답 결과 : 실패 시 false")
                )))

        // then
                .andDo(print());
    }

    @Test
    @DisplayName("팔로우 한 회원 목록 조회")
    @WithAuthUser
    void getFollowersTest() throws Exception {

        // given
        FollowerAllResponse response =
                new FollowerAllResponse(1L, 1L, GivenMember.GIVEN_NICKNAME, LocalDateTime.now());

        SliceImpl<FollowerAllResponse> slice =
                new SliceImpl<>(List.of(response), PageRequest.of(0, 10), true);

        when(followerService.getFollowers(anyLong(), any())).thenReturn(SliceCustomResponse.of(slice));

        // when
        mockMvc.perform(get("/api/v1/followers/memberId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("follower/getFollowers", responseFields(
                        fieldWithPath("data.[].followerId").description("팔로우 고유 식별자"),
                        fieldWithPath("data.[].memberId").description("회원 고유 식별자"),
                        fieldWithPath("data.[].nickname").description("회원 닉네임"),
                        fieldWithPath("data.[].createAt").description("팔로우 시간"),
                        fieldWithPath("pageSize").description("페이지 사이즈"),
                        fieldWithPath("number").description("페이지 숫자")
                )))

        // then
                .andDo(print());
    }

    @Test
    @DisplayName("팔로우 취소")
    @WithAuthUser
    void cancelTest() throws Exception {

        // given

        // when
        mockMvc.perform(delete("/api/v1/followers/1"))
                .andExpect(status().isNoContent())
                .andDo(document("follower/unFollow"))

        // then
                .andDo(print());
    }
}