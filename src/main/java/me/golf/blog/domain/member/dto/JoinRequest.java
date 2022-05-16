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

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequest {
    @JsonProperty("email")
    private Email email;

    @JsonProperty("password")
    private Password password;

    @JsonProperty("name")
    private Name name;

    @JsonProperty("nickname")
    private Nickname nickname;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy")
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
