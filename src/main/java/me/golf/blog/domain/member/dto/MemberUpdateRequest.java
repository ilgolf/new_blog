package me.golf.blog.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.domain.vo.Password;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberUpdateRequest {
    @JsonProperty("password")
    private Password password;

    @JsonProperty("nickname")
    private Nickname nickname;

    @JsonProperty("name")
    private Name name;

    public static MemberUpdateRequest of(final Password password, final Nickname nickname, final Name name) {
        return new MemberUpdateRequest(password, nickname, name);
    }

    public Member toEntity() {
        return Member.builder()
                .password(password)
                .nickname(nickname)
                .name(name)
                .build();
    }
}
