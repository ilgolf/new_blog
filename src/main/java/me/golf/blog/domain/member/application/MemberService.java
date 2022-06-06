package me.golf.blog.domain.member.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.dto.JoinResponse;
import me.golf.blog.domain.member.dto.MemberDTO;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.domain.memberCount.application.MemberCountService;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
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
    @Cacheable(key = "#email.email()", value = "getMember")
    @Transactional(readOnly = true)
    public MemberDTO getMember(final Email email) {
        log.debug("getMember");
        return memberRepository.findByEmailWithMemberDTO(email).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    // update
    public void update(final Member updateMember, final Long id) {
        memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND)).update(updateMember, encoder);
    }

    // delete
    public void delete(final Long memberId) {
        deleteCache(memberId);
        memberRepository.updateActivatedById(memberId, LocalDateTime.now());
    }

    @CacheEvict(value = "getMember", key = "#memberId")
    public void deleteCache(final Long memberId) {
        log.debug("회원 캐시 삭제 : {}", memberId);
    }
}
