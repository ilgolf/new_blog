package me.golf.blog.domain.board.domain.persist.express;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.StringUtils;

import static me.golf.blog.domain.board.domain.persist.QBoard.board;
import static me.golf.blog.domain.member.domain.persist.QMember.member;

public enum BoardExpression {
    EQ_NICKNAME {
        @Override
        public BooleanExpression eqBoardField(final String nickname) {
            if (!StringUtils.hasText(nickname)) {
                return null;
            }
            return member.nickname.nickname.like(nickname + "%");
        }
    },
    EQ_TITLE {
        @Override
        public BooleanExpression eqBoardField(final String title) {
            if (!StringUtils.hasText(title)) {
                return null;
            }
            return board.title.title.like(title + "%");
        }
    },
    EQ_CONTENT {
        @Override
        public BooleanExpression eqBoardField(final String content) {
            if (!StringUtils.hasText(content)) {
                return null;
            }
            return board.content.content.like(content + "%");
        }
    };

    public abstract BooleanExpression eqBoardField(final String keyword);
}
