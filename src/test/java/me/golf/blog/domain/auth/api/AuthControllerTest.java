package me.golf.blog.domain.auth.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.golf.blog.domain.auth.application.AuthService;
import me.golf.blog.domain.auth.dto.AccessTokenResponse;
import me.golf.blog.domain.auth.dto.LoginRequest;
import me.golf.blog.domain.member.WithAuthUser;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.domain.vo.RoleType;
import me.golf.blog.domain.member.util.GivenMember;
import me.golf.blog.global.jwt.TokenProvider;
import me.golf.blog.global.jwt.dto.TokenDTO;
import me.golf.blog.global.jwt.vo.AccessToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.Collections;
import java.util.Set;

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
@Transactional
class AuthControllerTest {
    @Autowired
    MemberRepository memberRepository;

    @MockBean
    AuthService authService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TokenProvider tokenProvider;

    static Long memberId;

    @BeforeEach
    public void init() {
        memberId = memberRepository.save(toEntity()).getId();
    }

    @Test
    @DisplayName("????????? ??????????????? ?????? ???????????????.")
    void login() throws Exception {
        // given
        LoginRequest request = LoginRequest.of(GIVEN_EMAIL, GIVEN_PASSWORD);
        String body = objectMapper.writeValueAsString(request);

        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + RoleType.USER));

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD, authorities);
        TokenDTO tokenDTO = tokenProvider.createToken(memberId, token);

        // when
        when(authService.login(any(), any())).thenReturn(tokenDTO);

        mockMvc.perform(post("/api/v1/public/auth/login").content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("auth/login",
                        requestFields(
                                fieldWithPath("email").description("?????? ?????????"),
                                fieldWithPath("password").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("????????? ??????")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ????????? ????????? ?????? ?????? ?????????.")
    void reissue() throws Exception {
        // given
        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + RoleType.USER));

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD, authorities);
        TokenDTO tokenDTO = tokenProvider.createToken(memberId, token);

        // when
        when(authService.reissue(any())).thenReturn(tokenDTO.getAccessToken());

        mockMvc.perform(post("/api/v1/public/auth/reissue")
                        .cookie(new Cookie("refreshToken", tokenDTO.getRefreshToken().refreshToken())))
                .andExpect(status().isOk())
                .andDo(document("auth/reissue",
                        responseFields(
                                fieldWithPath("accessToken").description("?????? ????????? ????????? ??????")
                        )))
                .andDo(print());
    }

    @Test
    @WithAuthUser
    @DisplayName("??????????????? ??????????????????.")
    void logout() throws Exception {
        // given
        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + RoleType.USER));

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD, authorities);
        TokenDTO tokenDTO = tokenProvider.createToken(memberId, token);

        // when
        mockMvc.perform(delete("/api/v1/auth/logout")
                        .cookie(new Cookie("refreshToken", tokenDTO.getRefreshToken().refreshToken())))
                .andExpect(status().isNoContent())
                .andDo(document("auth/logout"))
                .andDo(print());
    }
}