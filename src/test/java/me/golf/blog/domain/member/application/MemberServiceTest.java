package me.golf.blog.domain.member.application;

import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.domain.vo.*;
import me.golf.blog.domain.member.dto.*;
import me.golf.blog.domain.member.error.DuplicateNicknameException;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        JoinResponse joinResponse = memberService.create(toEntityWithCount());

        email = joinResponse.getEmail();
        memberId = joinResponse.getMemberId();
    }

    @Test
    @DisplayName("회원정보를 조회해온다.")
    void findOne() {
        // when
        MemberResponse member = memberReadService.getDetailBy(memberId);

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

        for (int i = 0; i < 20; i++) {
            final Member member = Member.builder()
                    .email(Email.from("member" + (i + 1) + "@naver.com"))
                    .password(Password.from("123456"))
                    .name(Name.from("kim" + (i + 1)))
                    .nickname(Nickname.from("kim3" + (i + 1)))
                    .birth(LocalDate.of(2001, 10, 25))
                    .role(RoleType.USER)
                    .build();

            memberService.create(member);
        }

        // when
        List<MemberAllResponse> members = memberReadService.getMembers(memberSearch, pageable).getData();

        // then
        assertThat(members.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("본인 별칭이 아닌 다른 겹치는 별칭이 들어오면 예외 발생")
    void updateDuplication() {
        // given
        Member newMember = Member.builder()
                .email(Email.from("member1@naver.com"))
                .password(Password.from("1234"))
                .name(Name.from("kim23"))
                .nickname(Nickname.from("kim3333"))
                .role(RoleType.USER)
                .birth(GIVEN_BIRTH)
                .build();

        MemberUpdateRequest requestMember = MemberUpdateRequest.of(GIVEN_PASSWORD, Nickname.from("kim3333"), Name.from("김티오"));

        // when
        memberService.create(newMember);

        // then
        assertThrows(DuplicateNicknameException.class, () -> memberService.update(requestMember.toEntity(), memberId));
    }

    @Test
    @DisplayName("키워드가 있으면 검색을 하여 조회한다.")
    void findAllWithKeyword() {
        // given
        MemberSearch memberSearch = new MemberSearch(null, GIVEN_EMAIL.email());
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<MemberAllResponse> members = memberReadService.getMembers(memberSearch, pageable).getData();

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
        Member member = memberRepository.findByEmail(email).orElseThrow(
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