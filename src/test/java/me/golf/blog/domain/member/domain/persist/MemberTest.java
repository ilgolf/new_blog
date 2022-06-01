package me.golf.blog.domain.member.domain.persist;

import me.golf.blog.domain.member.domain.util.TestPasswordEncoder;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.domain.vo.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static me.golf.blog.domain.member.domain.util.GivenMember.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("정보를 받아와 업데이트 한다.")
    void update() {
        // given
        Member updateMember = Member.builder()
                .password(Password.from("123456"))
                .nickname(Nickname.from("티오더"))
                .name(Name.from("김티오"))
                .build();

        PasswordEncoder encoder = TestPasswordEncoder.initialize();

        // when
        Member member = toEntity().update(updateMember, encoder);

        // then
        assertAll(() -> {
            assertTrue(encoder.matches(updateMember.getPassword().password(), member.getPassword().password()));
            assertEquals(member.getName(), updateMember.getName());
            assertEquals(member.getNickname(), updateMember.getNickname());
        });
    }

    @Test
    @DisplayName("회원 인코딩 테스트")
    void encodePassword() {
        // given
        final PasswordEncoder encoder = TestPasswordEncoder.initialize();

        // when
        Member member = toEntity().encode(encoder);


        // then
        assertAll(
                () -> assertThat(member.getPassword()).isNotEqualTo(PASSWORD),
                () -> assertThat(encoder.matches(PASSWORD.password(),
                        member.getPassword().password())).isTrue());
    }

    @Test
    @DisplayName("값이 같으면 같은 객체이다.")
    void equivalenceTest() {
        assertThat(EMAIL).isEqualTo(Email.from("golf@naver.com"));
        assertThat(PASSWORD).isEqualTo(Password.from("asdf12345"));
        assertThat(NICKNAME).isEqualTo(Nickname.from("kim"));
    }
}