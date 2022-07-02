package me.golf.blog.domain.member.domain.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.dto.MemberAllResponse;
import me.golf.blog.domain.member.dto.MemberDTO;
import me.golf.blog.domain.member.dto.MemberSearch;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static me.golf.blog.domain.member.domain.persist.QMember.*;
import static me.golf.blog.domain.memberCount.domain.persist.QMemberCount.*;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory query;

    public Optional<MemberDTO> findByEmailWithMemberDTO(final Email email) {
        return Optional.ofNullable(query.select(Projections.constructor(MemberDTO.class,
                        member.email,
                        member.name,
                        member.nickname,
                        member.birth,
                        member.memberCount.id.as("memberCountId")))
                .from(member)
                .join(member.memberCount, memberCount)
                .where(member.email.eq(email))
                .fetchOne());
    }

    public Optional<CustomUserDetails> findByEmail(final Email email) {
        return Optional.ofNullable(
                query.select(Projections.constructor(CustomUserDetails.class,
                                member.id.as("id"),
                                member.email,
                                member.role))
                        .from(member)
                        .where(member.email.eq(email))
                        .fetchOne());
    }

    public Optional<CustomUserDetails> findByIdWithDetails(Long memberId) {
        return Optional.ofNullable(
                query.select(Projections.constructor(CustomUserDetails.class,
                                member.id.as("id"),
                                member.email,
                                member.role))
                        .from(member)
                        .where(member.id.eq(memberId))
                        .fetchOne());
    }

    public PageCustomResponse<MemberAllResponse> findAllWithSearch(final MemberSearch memberSearch, final Pageable pageable) {
        List<MemberAllResponse> members = query.select(Projections.constructor(MemberAllResponse.class,
                        member.email,
                        member.nickname,
                        member.name))
                .from(member)
                .where(
                        eqNickname(memberSearch.getNickname()),
                        eqEmail(memberSearch.getEmail())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Member> count = query.select(member)
                .from(member);

        return PageCustomResponse.of(PageableExecutionUtils.getPage(members, pageable, () -> count.fetch().size()));
    }

    public Optional<Email> existByEmail(final Email email) {
        return Optional.ofNullable(query.select(member.email)
                .from(member)
                .where(member.email.eq(email))
                .limit(1)
                .fetchOne());
    }

    public Optional<Nickname> existByNickname(final Nickname nickname) {
        return Optional.ofNullable(query.select(member.nickname)
                .from(member)
                .where(member.nickname.eq(nickname))
                .limit(1)
                .fetchOne());
    }

    private BooleanExpression eqNickname(final String nickname) {
        if (!StringUtils.hasText(nickname)) {
            return null;
        }
        return member.nickname.nickname.contains(nickname);
    }

    private BooleanExpression eqEmail(final String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        return member.email.email.contains(email);
    }
}
