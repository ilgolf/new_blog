package me.golf.blog.domain.member.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.domain.redisform.MemberRedisDto;
import me.golf.blog.domain.member.domain.redisform.MemberRedisRepository;
import me.golf.blog.domain.member.dto.MemberAllResponse;
import me.golf.blog.domain.member.dto.MemberResponse;
import me.golf.blog.domain.member.dto.MemberSearch;
import me.golf.blog.domain.member.error.MemberNotFoundException;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.error.exception.ErrorCode;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberReadService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Cacheable(key = "#userDetails.getId()", value = "member")
    public MemberResponse getDetailBy(final CustomUserDetails userDetails) {

        Member member = memberRepository.findById(userDetails.getId()).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public PageCustomResponse<MemberAllResponse> getMembers(final MemberSearch memberSearch, final Pageable pageable) {

        return memberRepository.findAllWithSearch(memberSearch, pageable);
    }
}
