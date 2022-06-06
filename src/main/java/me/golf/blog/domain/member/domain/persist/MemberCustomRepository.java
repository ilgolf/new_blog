package me.golf.blog.domain.member.domain.persist;

import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.dto.MemberAllResponse;
import me.golf.blog.domain.member.dto.MemberDTO;
import me.golf.blog.domain.member.dto.MemberSearch;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberCustomRepository {
    Optional<MemberDTO> findByEmailWithMemberDTO(final Email email);
    Optional<CustomUserDetails> findByEmail(final Email email);
    Optional<CustomUserDetails> findByIdWithDetails(final Long memberId);

    List<MemberAllResponse> findAllWithSearch(final MemberSearch memberSearch, final Pageable pageable);
}
