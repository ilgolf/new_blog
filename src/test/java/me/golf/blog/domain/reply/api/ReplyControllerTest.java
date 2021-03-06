package me.golf.blog.domain.reply.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.golf.blog.domain.member.WithAuthUser;
import me.golf.blog.domain.reply.applicationa.ReplyService;
import me.golf.blog.domain.reply.domain.vo.Comment;
import me.golf.blog.domain.reply.dto.ReplyAllResponse;
import me.golf.blog.domain.reply.dto.ReplyCreateRequest;
import me.golf.blog.domain.reply.dto.ReplyUpdateRequest;
import me.golf.blog.global.common.PageCustomResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ReplyControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean ReplyService replyService;
    @Autowired MockMvc mockMvc;

    @Test
    @DisplayName("reply ?????? ???????????? ?????????")
    @WithAuthUser
    void create() throws Exception {
        ReplyCreateRequest request = new ReplyCreateRequest(Comment.from("???????????????, ????????? ?????????."));
        String body = objectMapper.writeValueAsString(request);

        when(replyService.create(any(), any(), any())).thenReturn(1L);

        mockMvc.perform(post("/api/v1/replies/1").content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("reply/create",
                        requestFields(
                                fieldWithPath("comment").description("?????? ??????")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("reply ?????? ?????? ?????????")
    void findAll() throws Exception {
        ReplyAllResponse replies = new ReplyAllResponse
                ("ilgolc@naver.com", Comment.from("???????????????, ????????? ?????????."), LocalDateTime.now());

        Page<ReplyAllResponse> responses =
                new PageImpl<>(List.of(replies), PageRequest.of(0, 10), 1);

        when(replyService.findAll(any(), any())).thenReturn(PageCustomResponse.of(responses));

        mockMvc.perform(get("/api/v1/public/replies/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("reply/findAll",
                        responseFields(
                                fieldWithPath("data.[].email").description("?????? ?????????"),
                                fieldWithPath("data.[].comment").description("?????? ??????"),
                                fieldWithPath("data.[].createDate").description("?????? ??????"),
                                fieldWithPath("totalPage").description("?????? ??? ?????????"),
                                fieldWithPath("pageSize").description("????????? ?????????"),
                                fieldWithPath("totalElements").description("????????? ??? ??????"),
                                fieldWithPath("number").description("????????? ??????")
                        )))
                .andDo(print());
    }

    @Test
    @WithAuthUser
    void update() throws Exception {
        // given
        ReplyUpdateRequest request = new ReplyUpdateRequest(Comment.from("???????????????, ????????? ?????????."));
        String body = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(patch("/api/v1/replies/1")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))

        // then
                .andExpect(status().isOk())
                .andDo(document("reply/update",
                        requestFields(
                                fieldWithPath("comment").description("????????? ?????? ??????")
                        )))
                .andDo(print());
    }

    @Test
    @WithAuthUser
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/v1/replies/1"))
                .andExpect(status().isNoContent())
                .andDo(document("reply/delete"))
                .andDo(print());
    }
}