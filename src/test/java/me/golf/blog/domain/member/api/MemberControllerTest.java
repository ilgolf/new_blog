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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @DisplayName("????????? ?????? ??????????????? ?????? ??????????????? ????????????.")
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
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("password").description("????????????"),
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("birth").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("?????? ?????? ?????????"),
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("name").description("??????")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? null?????? BindingError ??????")
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
    @DisplayName("????????? ?????? ??????????????? ?????? ??????????????? ????????????.")
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
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("name").description("??????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("age").description("??????"),
                                fieldWithPath("followerCount").description("????????? ???"),
                                fieldWithPath("followingCount").description("????????? ???"),
                                fieldWithPath("boardCount").description("????????? ???")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("??????????????? findAll ??????????????? ????????????.")
    void findAllTest() throws Exception {
        List<MemberAllResponse> members = List.of(new MemberAllResponse(GIVEN_EMAIL, GIVEN_NICKNAME, GIVEN_NAME));

        PageCustomResponse<MemberAllResponse> response
                = PageCustomResponse.of(new PageImpl<>(members, PageRequest.of(0, 10), 1));

        when(memberReadService.findAll(any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/public/members").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/findAll",
                        responseFields(
                                fieldWithPath("data.[].email").description("?????? ?????????"),
                                fieldWithPath("data.[].nickname").description("?????? ??????"),
                                fieldWithPath("data.[].name").description("?????? ??????"),
                                fieldWithPath("totalPage").description("?????? ??? ?????????"),
                                fieldWithPath("pageSize").description("????????? ?????????"),
                                fieldWithPath("totalElements").description("????????? ??? ??????"),
                                fieldWithPath("number").description("????????? ??????")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ????????? ?????? ????????? ???????????????")
    void findSearch() throws Exception {
        List<MemberAllResponse> members = List.of(new MemberAllResponse(GIVEN_EMAIL, GIVEN_NICKNAME, GIVEN_NAME));

        PageCustomResponse<MemberAllResponse> response
                = PageCustomResponse.of(new PageImpl<>(members, PageRequest.of(0, 10), 1));

        when(memberReadService.findAll(any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/public/members")
                        .param("nickname", GIVEN_NICKNAME.nickname())
                        .param("email", GIVEN_EMAIL.email())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/search",
                        requestParameters(
                                parameterWithName("nickname").description("?????? ?????? ????????? ?????????"),
                                parameterWithName("email").description("?????? ?????? ????????? ?????????")),
                        responseFields(
                                fieldWithPath("data.[].email").description("?????? ?????????"),
                                fieldWithPath("data.[].nickname").description("?????? ??????"),
                                fieldWithPath("data.[].name").description("?????? ??????"),
                                fieldWithPath("totalPage").description("?????? ??? ?????????"),
                                fieldWithPath("pageSize").description("????????? ?????????"),
                                fieldWithPath("totalElements").description("????????? ??? ??????"),
                                fieldWithPath("number").description("????????? ??????")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("????????? ?????? ??????????????? ??????????????? ????????????.")
    @WithAuthUser
    void updateTest() throws Exception {
        MemberUpdateRequest request = MemberUpdateRequest.of
                (Password.from("123456"), Nickname.from("?????????"), Name.from("?????????"));
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/v1/members").content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/update",
                        requestFields(
                                fieldWithPath("password").description("????????????"),
                                fieldWithPath("nickname").description("?????????"),
                                fieldWithPath("name").description("??????")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("????????? ?????? ??????????????? ????????? ????????????.")
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