package me.golf.blog.domain.like.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.golf.blog.domain.board.dto.LikeAllResponse;
import me.golf.blog.domain.like.application.LikeService;
import me.golf.blog.domain.member.WithAuthUser;
import me.golf.blog.domain.member.util.GivenMember;
import me.golf.blog.global.common.SliceCustomResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureRestDocs
@SpringBootTest
class LikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LikeService likeService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("게시판 좋아요")
    @WithAuthUser
    void likeBoard() throws Exception {

        // given
        when(likeService.likeBoard(anyLong(), anyLong())).thenReturn(1L);

        // when
        mockMvc.perform(post("/api/v1/likes/1"))
                .andExpect(status().isCreated())
                .andDo(document("like/create"))

        // then
                .andDo(print());
    }

    @Test
    @DisplayName("게시판 좋아요 목록 조회")
    @WithAuthUser
    void getBoardLikeList() throws Exception {

        // given
        LikeAllResponse response = new LikeAllResponse(1L, GivenMember.GIVEN_NICKNAME);
        Slice<LikeAllResponse> slices =
                new SliceImpl<>(List.of(response), PageRequest.of(0, 10), true);

        when(likeService.getBoardLikeList(anyLong(), any())).thenReturn(SliceCustomResponse.of(slices));

        // when
        mockMvc.perform(get("/api/v1/likes/boards/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("like/likeList", responseFields(
                        fieldWithPath("data.[].memberId").description("회원 아이디"),
                        fieldWithPath("data.[].nickname").description("회원 닉네임"),
                        fieldWithPath("pageSize").description("페이지 사이즈"),
                        fieldWithPath("number").description("페이지 번호")
                )))

        // then
                .andDo(print());
    }

    @Test
    @DisplayName("게시판 좋아요 취소")
    @WithAuthUser
    void unLikeBoard() throws Exception {

        // when
        mockMvc.perform(delete("/api/v1/likes/1"))
                .andDo(document("like/delete"))
                .andExpect(status().isNoContent())

        // then
                .andDo(print());
    }
}