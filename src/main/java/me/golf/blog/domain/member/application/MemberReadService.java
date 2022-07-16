package me.golf.blog.domain.member.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.dto.MemberAllResponse;
import me.golf.blog.domain.member.dto.MemberDTO;
import me.golf.blog.domain.member.dto.MemberResponse;
import me.golf.blog.domain.member.dto.MemberSearch;
import me.golf.blog.domain.member.error.MemberCountNotFoundException;
import me.golf.blog.domain.memberCount.domain.persist.MemberCount;
import me.golf.blog.domain.memberCount.domain.persist.MemberCountRepository;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberReadService {
    private final MemberService memberService;
    private final MemberCountRepository memberCountRepository;
    private final MemberRepository memberRepository;

    public MemberResponse findByEmail(final Email email) {
        MemberDTO member = memberService.getMember(email);
        MemberCount memberCount = memberCountRepository.findById(member.getMemberCountId())
                .orElseThrow(() -> new MemberCountNotFoundException(ErrorCode.USER_COUNT_NOT_FOUND));

        return MemberResponse.of(member, memberCount);
    }

    public PageCustomResponse<MemberAllResponse> findAll(final MemberSearch memberSearch, final Pageable pageable) {
        return memberRepository.findAllWithSearch(memberSearch, pageable);
    }
}
