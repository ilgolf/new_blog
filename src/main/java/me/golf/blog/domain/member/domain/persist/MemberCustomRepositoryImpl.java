package me.golf.blog.domain.member.domain.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.member.domain.persist.express.MemberExpression;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.domain.member.dto.MemberAllResponse;
import me.golf.blog.domain.member.dto.MemberDTO;
import me.golf.blog.domain.member.dto.MemberSearch;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.security.principal.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static me.golf.blog.domain.member.domain.persist.QMember.*;
import static me.golf.blog.domain.member.domain.persist.express.MemberExpression.*;
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
                        member.memberCount.followerCount,
                        member.memberCount.boardCount,
                        member.memberCount.followingCount))
                .from(member)
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
                        EQ_NICKNAME.eqMemberField(memberSearch.getNickname()),
                        EQ_EMAIL.eqMemberField(memberSearch.getEmail())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (members.size() == 0) {
            return PageCustomResponse.of(Page.empty());
        }

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

    @Override
    public void increaseBoardCount(final Long memberId) {
        query.update(member).set(member.memberCount.boardCount, member.memberCount.boardCount.add(1))
                .where(member.id.eq(memberId)).execute();
    }
}
