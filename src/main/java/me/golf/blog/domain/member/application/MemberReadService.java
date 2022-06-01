package me.golf.blog.domain.member.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.dto.MemberDTO;
import me.golf.blog.domain.member.dto.MemberResponse;
import me.golf.blog.domain.member.error.MemberCountNotFoundException;
import me.golf.blog.domain.memberCount.domain.persist.MemberCount;
import me.golf.blog.domain.memberCount.domain.persist.MemberCountRepository;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberReadService {
    private final MemberService memberService;
    private final MemberCountRepository memberCountRepository;

    public MemberResponse findById(final Long memberId) {
        MemberDTO member = memberService.getMember(memberId);
        MemberCount memberCount = memberCountRepository.findById(member.getMemberCountId())
                .orElseThrow(() -> new MemberCountNotFoundException(ErrorCode.USER_COUNT_NOT_FOUND));

        return MemberResponse.of(member, memberCount);
    }
}
