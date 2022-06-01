package me.golf.blog.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;
import me.golf.blog.domain.member.domain.vo.Nickname;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class MemberDTO implements Serializable {
    private final long serialVersionUID = 1L;

    @JsonProperty("email")
    private Email email;

    @JsonProperty("name")
    private Name name;

    @JsonProperty("nickname")
    private Nickname nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate birth;

    private Long memberCountId;
}
