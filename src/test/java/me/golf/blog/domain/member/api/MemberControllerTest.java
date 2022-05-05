package me.golf.blog.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.golf.blog.domain.member.WithAuthUser;
import me.golf.blog.domain.member.application.MemberService;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.dto.JoinRequest;
import me.golf.blog.domain.member.dto.JoinResponse;
import me.golf.blog.domain.member.dto.MemberResponse;
import me.golf.blog.domain.member.dto.MemberUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static me.golf.blog.domain.member.util.GivenMember.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("요청을 받아 정상적으로 생성 컨트롤러가 동작한다.")
    void createTest() throws Exception {
        JoinRequest joinRequest = JoinRequest.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .name(GIVEN_NAME)
                .nickname(GIVEN_NICKNAME)
                .birth(GIVEN_BIRTH)
                .build();

        String body = objectMapper.writeValueAsString(joinRequest);

        JoinResponse response = JoinResponse.of(joinRequest.toEntity());

        when(memberService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/public/members").content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("/member/create",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("패스워드"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("birth").description("생년월일")
                        ),
                        responseFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("name").description("이름")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("요청을 받아 정상적으로 조회 컨트롤러가 동작한다.")
    @WithAuthUser
    void findMemberTest() throws Exception {
        when(memberService.findOne(any())).thenReturn(MemberResponse.of(toEntity()));

        mockMvc.perform(get("/api/v1/members/id").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("/member/findById",
                        responseFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("age").description("나이")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("요청을 받아 정상적으로 업데이트가 동작한다.")
    @WithAuthUser
    void updateTest() throws Exception {
        MemberUpdateRequest request = MemberUpdateRequest.of
                (Email.from("ilgoll@naver.com"), Nickname.from("티오더"), Name.from("김티오"));
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/v1/members").content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("/member/update",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("name").description("이름")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("요청을 받아 정상적으로 회원이 삭제된다.")
    @WithAuthUser
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/v1/members"))
                .andExpect(status().isNoContent())
                .andDo(document("/member/delete"))
                .andDo(print());
    }
}