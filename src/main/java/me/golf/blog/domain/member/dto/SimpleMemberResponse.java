package me.golf.blog.domain.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Name;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleMemberResponse {
    private Long memberId;
    private Email email;
    private Name name;

    public static SimpleMemberResponse of(final Member member) {
        return new SimpleMemberResponse(member.getId(), member.getEmail(), member.getName());
    }
}
