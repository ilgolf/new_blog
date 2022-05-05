package me.golf.blog.domain.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinResponse {

    @NotBlank(message = "필수 값입니다.")
    private Email email;

    @NotBlank(message = "필수 값입니다.")
    private Name name;

    public static JoinResponse of(final Member member) {
        return new JoinResponse(member.getEmail(), member.getName());
    }
}
