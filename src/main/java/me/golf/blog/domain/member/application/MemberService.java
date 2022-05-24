package me.golf.blog.domain.member.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.dto.JoinResponse;
import me.golf.blog.domain.member.dto.MemberDTO;
import me.golf.blog.domain.member.dto.MemberResponse;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.domain.memberCount.application.MemberCountService;
import me.golf.blog.domain.memberCount.domain.persist.MemberCountRepository;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final MemberCountService memberCountService;

    // create
    public JoinResponse create(final Member member) {
        memberCountService.saveMemberCount(member);
        return JoinResponse.of(memberRepository.save(member.encode(encoder)));
    }

    // find
    @Cacheable(key = "#memberId", value = "getMember")
    public MemberDTO getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberDTO::of)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    // update
    public void update(final Member updateMember, final Long id) {
        memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND)).update(updateMember);
    }

    // delete
    public void delete(final Long id) {
        memberRepository.deleteById(id);
    }
}
