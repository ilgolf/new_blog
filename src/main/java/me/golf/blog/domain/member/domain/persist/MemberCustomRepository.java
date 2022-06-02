package me.golf.blog.domain.member.domain.persist;

import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.dto.MemberDTO;
import me.golf.blog.global.security.principal.CustomUserDetails;

import java.util.Optional;

public interface MemberCustomRepository {
    Optional<MemberDTO> findByIdWithMemberDTO(Long memberId);
    Optional<CustomUserDetails> findByEmail(final Email email);
    Optional<CustomUserDetails> findByIdWithDetails(Long memberId);
}
