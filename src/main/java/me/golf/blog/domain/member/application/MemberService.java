package me.golf.blog.domain.member.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.dto.JoinResponse;
import me.golf.blog.domain.member.dto.MemberDTO;
import me.golf.blog.domain.member.error.DuplicateEmailException;
import me.golf.blog.domain.member.error.DuplicateNicknameException;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.domain.memberCount.application.MemberCountService;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        existEmail(member.getEmail());
        existNickname(member.getNickname());
        member.addMemberCount(memberCountService.saveMemberCount());
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
    public void update(final Member updateMember, final Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (member.getNickname() != updateMember.getNickname()) {
            existNickname(updateMember.getNickname());
        }

        member.update(updateMember, encoder);
    }

    // delete
    public void delete(final Long memberId) {
        deleteCache(memberId);
        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND))
                .delete();
    }

    private void existEmail(final Email email) {
        memberRepository.existByEmail(email).ifPresent(member -> {
            throw new DuplicateEmailException(ErrorCode.DUPLICATE_EMAIL);
        });
    }

    private void existNickname(final Nickname nickname) {
        memberRepository.existByNickname(nickname).ifPresent(member -> {
            throw new DuplicateNicknameException(ErrorCode.DUPLICATE_NICKNAME);
        });
    }

    @CacheEvict(value = "getMember", key = "#memberId")
    public void deleteCache(final Long memberId) {
        log.debug("회원 캐시 삭제 : {}", memberId);
    }
}
