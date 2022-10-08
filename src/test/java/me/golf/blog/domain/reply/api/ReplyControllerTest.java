package me.golf.blog.domain.reply.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.golf.blog.domain.member.WithAuthUser;
import me.golf.blog.domain.member.util.GivenMember;
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
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ReplyControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean ReplyService replyService;
    @Autowired MockMvc mockMvc;

    @Test
    @DisplayName("reply 생성 컨트롤러 테스트")
    @WithAuthUser
    void create() throws Exception {
        ReplyCreateRequest request = new ReplyCreateRequest(Comment.from("안녕하세요, 테스트 입니다."));
        String body = objectMapper.writeValueAsString(request);

        when(replyService.create(any(), any(), any())).thenReturn(1L);

        mockMvc.perform(post("/api/v1/replies/1").content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("reply/create",
                        requestFields(
                                fieldWithPath("comment").description("댓글 내용")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("reply 전체 조회 테스트")
    void findAll() throws Exception {
        ReplyAllResponse replies =
                new ReplyAllResponse(Comment.from("안녕하세요, 테스트 입니다."),
                        LocalDateTime.now(), GivenMember.GIVEN_EMAIL);

        Page<ReplyAllResponse> responses =
                new PageImpl<>(List.of(replies), PageRequest.of(0, 10), 1);

        when(replyService.findAll(any(), any())).thenReturn(PageCustomResponse.of(responses));

        mockMvc.perform(get("/api/v1/public/replies/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("reply/findAll",
                        responseFields(
                                fieldWithPath("data.[].comment").description("댓글 내용"),
                                fieldWithPath("data.[].createdBy").description("작성자"),
                                fieldWithPath("data.[].createDate").description("생성 시간"),
                                fieldWithPath("totalPage").description("목록 총 페이지"),
                                fieldWithPath("pageSize").description("페이지 사이즈"),
                                fieldWithPath("totalElements").description("데이터 총 개수"),
                                fieldWithPath("number").description("페이지 넘버")
                        )))
                .andDo(print());
    }

    @Test
    @WithAuthUser
    void update() throws Exception {
        // given
        ReplyUpdateRequest request = new ReplyUpdateRequest(Comment.from("안녕하세요, 테스츠 입니다."));
        String body = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(patch("/api/v1/replies/1")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))

        // then
                .andExpect(status().isOk())
                .andDo(document("reply/update",
                        requestFields(
                                fieldWithPath("comment").description("수정될 변경 내용")
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