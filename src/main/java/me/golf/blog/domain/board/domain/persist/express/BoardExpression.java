package me.golf.blog.domain.board.domain.persist.express;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.StringUtils;

import static me.golf.blog.domain.board.domain.persist.QBoard.board;

public enum BoardExpression {
    EQ_NICKNAME {
        @Override
        public BooleanExpression eqBoardField(String keyword) {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            return board.member.nickname.nickname.contains(keyword);
        }
    },
    EQ_TITLE {
        @Override
        public BooleanExpression eqBoardField(String keyword) {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            return board.title.title.contains(keyword);
        }
    },
    EQ_CONTENT {
        @Override
        public BooleanExpression eqBoardField(String keyword) {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            return board.content.content.contains(keyword);
        }
    };

    public abstract BooleanExpression eqBoardField(final String keyword);
}
