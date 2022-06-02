package me.golf.blog.domain.memberCount.application;

import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.memberCount.domain.persist.MemberCount;
import me.golf.blog.domain.memberCount.domain.persist.MemberCountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCountService {
    private final MemberCountRepository memberCountRepository;

    public void saveMemberCount(final Member member) {
        MemberCount memberCount = MemberCount.createMemberCount(member);
        memberCountRepository.save(memberCount);
    }

    public void increaseBoardCount(final Member member) {
        memberCountRepository.updateBoardCount(member.getMemberCount().getId());
    }
}
