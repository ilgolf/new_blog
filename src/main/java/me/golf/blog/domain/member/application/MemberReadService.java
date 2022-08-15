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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberReadService {
    private final MemberRepository memberRepository;
    private final MemberRedisRepository memberRedisRepository;

    @Transactional(readOnly = true)
    public MemberResponse getDetailBy(final Long memberId) {
        MemberRedisDto member = memberRedisRepository.findDtoById(memberId).orElseGet(() -> {
            Member memberFromDB = memberRepository.findById(memberId).orElseThrow(
                    () -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

            return MemberRedisDto.of(memberFromDB);
        });

        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public PageCustomResponse<MemberAllResponse> findAll(final MemberSearch memberSearch, final Pageable pageable) {
        return memberRepository.findAllWithSearch(memberSearch, pageable);
    }
}
