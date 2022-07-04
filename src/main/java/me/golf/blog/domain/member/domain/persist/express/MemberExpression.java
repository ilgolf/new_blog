package me.golf.blog.domain.member.domain.persist.express;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.StringUtils;

import static me.golf.blog.domain.member.domain.persist.QMember.member;

public enum MemberExpression {
    EQ_NICKNAME{
        @Override
        public BooleanExpression eqMemberField(String nickname) {
            if (!StringUtils.hasText(nickname)) {
                return null;
            }
            return member.nickname.nickname.contains(nickname);        }
    },
    EQ_EMAIL {
        @Override
        public BooleanExpression eqMemberField(String email) {
            if (!StringUtils.hasText(email)) {
                return null;
            }
            return member.email.email.contains(email);
        }
    };

    public abstract BooleanExpression eqMemberField(final String keyword);
}
