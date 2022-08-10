package me.golf.blog.domain.member.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.dto.MemberAllResponse;
import me.golf.blog.domain.member.dto.MemberDTO;
import me.golf.blog.domain.member.dto.MemberResponse;
import me.golf.blog.domain.member.dto.MemberSearch;
import me.golf.blog.global.common.PageCustomResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberReadService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public MemberResponse findByEmail(final Email email) {
        MemberDTO member = memberService.getMember(email);

        return MemberResponse.of(member);
    }

    public PageCustomResponse<MemberAllResponse> findAll(final MemberSearch memberSearch, final Pageable pageable) {
        return memberRepository.findAllWithSearch(memberSearch, pageable);
    }
}
