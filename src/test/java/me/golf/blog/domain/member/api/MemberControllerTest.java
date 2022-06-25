package me.golf.blog.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.golf.blog.domain.member.WithAuthUser;
import me.golf.blog.domain.member.application.MemberReadService;
import me.golf.blog.domain.member.application.MemberService;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.domain.vo.Password;
import me.golf.blog.domain.member.dto.*;
import me.golf.blog.domain.memberCount.domain.persist.MemberCount;
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
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static me.golf.blog.domain.member.util.GivenMember.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MemberControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean MemberService memberService;
    @Autowired ObjectMapper objectMapper;
    @MockBean MemberReadService memberReadService;

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

        getCreate(body)
                .andExpect(status().isCreated())
                .andDo(document("member/create",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("패스워드"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("birth").description("생년월일")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("회원 고유 식별자"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("name").description("이름")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("하나라도 null이면 BindingError 발생")
    void createBinding() throws Exception {
        JoinRequest joinRequest = JoinRequest.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .name(null)
                .nickname(GIVEN_NICKNAME)
                .birth(GIVEN_BIRTH)
                .build();

        String body = objectMapper.writeValueAsString(joinRequest);

        JoinResponse response = JoinResponse.of(joinRequest.toEntity());

        when(memberService.create(any())).thenReturn(response);

        getCreate(body).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("요청을 받아 정상적으로 조회 컨트롤러가 동작한다.")
    void findMemberTest() throws Exception {
        MemberDTO memberDTO = MemberDTO.builder()
                .email(GIVEN_EMAIL)
                .name(GIVEN_NAME)
                .nickname(GIVEN_NICKNAME)
                .birth(LocalDate.of(1996, 10, 25))
                .memberCountId(1L)
                .build();
        when(memberReadService.findByEmail(any())).thenReturn(MemberResponse.of(memberDTO, new MemberCount()));

        mockMvc.perform(get("/api/v1/public/members/" + GIVEN_EMAIL.email()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/findById",
                        responseFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("age").description("나이"),
                                fieldWithPath("followerCount").description("팔로워 수"),
                                fieldWithPath("followingCount").description("팔로잉 수"),
                                fieldWithPath("boardCount").description("게시물 수")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("정상적으로 findAll 컨트롤러가 동작한다.")
    void findAllTest() throws Exception {
        List<MemberAllResponse> responses = List.of(new MemberAllResponse(GIVEN_EMAIL, GIVEN_NICKNAME, GIVEN_NAME));

        when(memberReadService.findAll(any(), any())).thenReturn(responses);

        mockMvc.perform(get("/api/v1/public/members").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/findAll",
                        responseFields(
                                fieldWithPath("[].email").description("회원 이메일"),
                                fieldWithPath("[].nickname").description("회원 별칭"),
                                fieldWithPath("[].name").description("회원 이름")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("검색 조건이 걸릴 경우의 인수테스트")
    void findSearch() throws Exception {
        List<MemberAllResponse> responses = List.of(new MemberAllResponse(GIVEN_EMAIL, GIVEN_NICKNAME, GIVEN_NAME));

        when(memberReadService.findAll(any(), any())).thenReturn(responses);

        mockMvc.perform(get("/api/v1/public/members")
                        .param("nickname", GIVEN_NICKNAME.nickname())
                        .param("email", GIVEN_EMAIL.email())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/search",
                        requestParameters(
                                parameterWithName("nickname").description("검색 회원 닉네임 키워드"),
                                parameterWithName("email").description("검색 회원 이메일 키워드")),
                        responseFields(
                                fieldWithPath("[].email").description("회원 이메일"),
                                fieldWithPath("[].nickname").description("회원 별칭"),
                                fieldWithPath("[].name").description("회원 이름")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("요청을 받아 정상적으로 업데이트가 동작한다.")
    @WithAuthUser
    void updateTest() throws Exception {
        MemberUpdateRequest request = MemberUpdateRequest.of
                (Password.from("123456"), Nickname.from("티오더"), Name.from("김티오"));
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/v1/members").content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/update",
                        requestFields(
                                fieldWithPath("password").description("비밀번호"),
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
                .andDo(document("member/delete"))
                .andDo(print());
    }

    private ResultActions getCreate(String body) throws Exception {
        return mockMvc.perform(post("/api/v1/public/members").content(body)
                .contentType(MediaType.APPLICATION_JSON));
    }
}