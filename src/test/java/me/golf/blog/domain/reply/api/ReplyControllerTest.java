package me.golf.blog.domain.reply.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.golf.blog.domain.member.WithAuthUser;
import me.golf.blog.domain.reply.applicationa.ReplyService;
import me.golf.blog.domain.reply.domain.vo.Comment;
import me.golf.blog.domain.reply.dto.ReplyAllResponse;
import me.golf.blog.domain.reply.dto.ReplyCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    @MockBean
    ReplyService replyService;
    @Autowired
    MockMvc mockMvc;

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
        ReplyAllResponse response = new ReplyAllResponse
                ("ilgolc@naver.com", Comment.from("안녕하세요, 테스트 입니다."), LocalDateTime.now());

        when(replyService.findAll(any(), any())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/public/replies/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("reply/findAll",
                        responseFields(
                                fieldWithPath("[].email").description("회원 이메일"),
                                fieldWithPath("[].comment").description("댓글 내용"),
                                fieldWithPath("[].createDate").description("생성 시간")
                        )))
                .andDo(print());
    }

    @Test
    void update() {

    }

    @Test
    void delete() {
    }
}