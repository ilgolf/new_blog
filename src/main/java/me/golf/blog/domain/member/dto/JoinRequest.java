package me.golf.blog.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.vo.RoleType;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.domain.vo.Password;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequest {
    @Valid
    @NotNull(message = "필수 값입니다. - email")
    private Email email;

    @Valid
    @NotNull(message = "필수 값입니다. - password")
    private Password password;

    @Valid
    @NotNull(message = "필수 값입니다. - name")
    private Name name;

    @Valid
    @NotNull(message = "필수 값입니다. - nickname")
    private Nickname nickname;

    @NotNull(message = "필수 값입니다. - birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .role(RoleType.USER)
                .birth(birth)
                .build();
    }
}
