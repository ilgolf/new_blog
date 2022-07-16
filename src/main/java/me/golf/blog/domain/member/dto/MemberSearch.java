package me.golf.blog.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSearch {
    private String nickname;
    private String email;
}
