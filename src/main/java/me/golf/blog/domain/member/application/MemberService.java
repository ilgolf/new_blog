package me.golf.blog.domain.member.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.dto.JoinResponse;
import me.golf.blog.domain.member.dto.MemberResponse;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    // create
    public JoinResponse create(final Member member) {
        return JoinResponse.of(memberRepository.save(member.encode(encoder)));
    }

    // find
    @Cacheable(key = "#id", value = "findOne")
    public MemberResponse findOne(final Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::of)
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
