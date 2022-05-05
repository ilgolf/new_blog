package me.golf.blog.domain.auth.application;

import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.global.jwt.TokenProvider;
import me.golf.blog.global.jwt.dto.TokenDTO;
import me.golf.blog.global.jwt.vo.AccessToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static me.golf.blog.domain.member.util.GivenMember.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        memberRepository.save(toEntity());
    }

    @Test
    void login() {
        // given, when
        TokenDTO token = authService.login(GIVEN_EMAIL, GIVEN_PASSWORD);

        // then
        assertThat(token.getAccessToken()).isNotNull();
        assertThat(token.getRefreshToken()).isNotNull();
        assertThat(tokenProvider.validateToken(token.getAccessToken().accessToken())).isTrue();
    }

    @Test
    void reissue() {
        // given
        TokenDTO token = authService.login(GIVEN_EMAIL, GIVEN_PASSWORD);

        // when
        AccessToken newToken = authService.reissue(token.getRefreshToken().refreshToken());

        // then
        assertThat(newToken).isNotNull();
        assertThat(tokenProvider.validateToken(newToken.accessToken())).isTrue();
    }
}