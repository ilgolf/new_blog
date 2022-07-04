package me.golf.blog.domain.board.domain.persist.express;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.StringUtils;

import static me.golf.blog.domain.board.domain.persist.QBoard.board;

public enum BoardExpression {
    EQ_NICKNAME {
        @Override
        public BooleanExpression eqBoardField(String nickname) {
            if (!StringUtils.hasText(nickname)) {
                return null;
            }
            return board.member.nickname.nickname.contains(nickname);
        }
    },
    EQ_TITLE {
        @Override
        public BooleanExpression eqBoardField(String title) {
            if (!StringUtils.hasText(title)) {
                return null;
            }
            return board.title.title.contains(title);
        }
    },
    EQ_CONTENT {
        @Override
        public BooleanExpression eqBoardField(String content) {
            if (!StringUtils.hasText(content)) {
                return null;
            }
            return board.content.content.contains(content);
        }
    };

    public abstract BooleanExpression eqBoardField(final String keyword);
}
