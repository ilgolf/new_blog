package me.golf.blog.domain.member.application;

import me.golf.blog.domain.member.dto.MemberUpdateRequest;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.dto.JoinResponse;
import me.golf.blog.domain.member.dto.MemberResponse;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static me.golf.blog.domain.member.util.GivenMember.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 전체 테스트 시 IDENTITY가 증가하므로 1L, 2L, 3L
 * 한개 씩 단위테스트 해볼 땐 1L로 테스트하세요.
 */
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberReadService memberReadService;

    @BeforeEach
    void setUp() {
        JoinResponse joinResponse = memberService.create(toEntity());
        System.out.println("joinResponse.getEmail() = " + joinResponse.getEmail().email());
    }

    @Test
    @DisplayName("회원정보를 조회해온다.")
    void findOne() {
        // when
        MemberResponse member = memberReadService.findById(2L);

        // then
        assertThat(member.getEmail()).isEqualTo(GIVEN_EMAIL);
        assertThat(member.getName()).isEqualTo(GIVEN_NAME);
    }

    @Test
    @DisplayName("회원 정보를 업데이트 한다.")
    void update() {
        // given
        MemberUpdateRequest updateRequest = MemberUpdateRequest.of(Email.from("ilgolf@naver.com"), Nickname.from("torder"), Name.from("김티오"));

        // when
        memberService.update(updateRequest.toEntity(), 3L);
        MemberResponse member = memberReadService.findById(3L);

        // then
        assertThat(member.getEmail()).isEqualTo(Email.from("ilgolf@naver.com"));
        assertThat(member.getNickname()).isNotEqualTo(GIVEN_NICKNAME);
        assertThat(member.getName()).isEqualTo(Name.from("김티오"));
    }

    @Test
    @DisplayName("회원 정보를 삭제한다.")
    void delete() {
        // when
        memberService.delete(1L);

        // then
        assertThrows(MemberNotFoundException.class, () -> memberReadService.findById(1L));
    }
}