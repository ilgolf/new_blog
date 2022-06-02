package me.golf.blog.domain.member.domain.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Password;
import me.golf.blog.domain.member.dto.MemberDTO;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static me.golf.blog.domain.member.domain.persist.QMember.*;
import static me.golf.blog.domain.memberCount.domain.persist.QMemberCount.*;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {
    private final JPAQueryFactory query;

    public Optional<MemberDTO> findByIdWithQuery(final Long memberId) {
        MemberDTO memberDTO = query.select(Projections.constructor(MemberDTO.class,
                        memberCount.member.email,
                        memberCount.member.name,
                        memberCount.member.nickname,
                        memberCount.member.birth,
                        memberCount.id.as("memberCountId")))
                .from(memberCount)
                .join(memberCount.member, member)
                .where(memberCount.member.id.eq(memberId))
                .fetchOne();

        return Optional.ofNullable(memberDTO);
    }

    public Optional<CustomUserDetails> findByEmail(final Email email) {
        return Optional.ofNullable(
                query.select(Projections.constructor(CustomUserDetails.class,
                                member.id.as("id"),
                                member.email,
                                member.password,
                                member.role))
                        .from(member)
                        .where(member.email.eq(email))
                        .fetchOne()
        );
    }

    public Optional<CustomUserDetails> findById(Long memberId) {
        return Optional.ofNullable(
                query.select(Projections.constructor(CustomUserDetails.class,
                                member.id.as("id"),
                                member.email,
                                member.password,
                                member.role))
                        .from(member)
                        .where(member.id.eq(memberId))
                        .fetchOne()
        );
    }
}
