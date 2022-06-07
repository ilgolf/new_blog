package me.golf.blog.domain.member.application;

import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Password;
import me.golf.blog.domain.member.dto.*;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired MemberRepository memberRepository;

    static Email email;
    static Long memberId;

    @BeforeEach
    void setUp() {
        JoinResponse joinResponse = memberService.create(toEntity());

        email = joinResponse.getEmail();
        memberId = joinResponse.getMemberId();
    }

    @Test
    @DisplayName("회원정보를 조회해온다.")
    void findOne() {
        // when
        MemberResponse member = memberReadService.findByEmail(email);

        // then
        assertThat(member.getEmail()).isEqualTo(GIVEN_EMAIL);
        assertThat(member.getName()).isEqualTo(GIVEN_NAME);
    }

    @Test
    @DisplayName("키워드가 없으면 회원 전체적인 정보를 들고온다.")
    void findAll() {
        // given
        MemberSearch memberSearch = new MemberSearch(null, null);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<MemberAllResponse> members = memberReadService.findAll(memberSearch, pageable);

        // then
        assertThat(members.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("키워드가 있으면 검색을 하여 조회한다.")
    void findAllWithKeyword() {
        // given
        MemberSearch memberSearch = new MemberSearch(null, GIVEN_EMAIL.email());
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<MemberAllResponse> members = memberReadService.findAll(memberSearch, pageable);

        // then
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원 정보를 업데이트 한다.")
    void update() {
        // given
        MemberUpdateRequest updateRequest = MemberUpdateRequest.of(Password.from("123456"), Nickname.from("torder"), Name.from("김티오"));

        // when
        memberService.update(updateRequest.toEntity(), memberId);
        MemberDTO member = memberRepository.findByEmailWithMemberDTO(email).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

        // then
        assertThat(member.getNickname()).isNotEqualTo(GIVEN_NICKNAME);
        assertThat(member.getName()).isEqualTo(Name.from("김티오"));
    }

    @Test
    @DisplayName("회원 정보를 삭제한다.")
    void delete() {
        // when
        memberService.delete(memberId);

        // then
        assertThrows(MemberNotFoundException.class, () -> memberService.getMember(email));
    }
}