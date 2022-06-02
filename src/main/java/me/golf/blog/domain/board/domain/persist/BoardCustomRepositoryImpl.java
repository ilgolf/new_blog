package me.golf.blog.domain.board.domain.persist;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.dto.BoardAllResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static me.golf.blog.domain.board.domain.persist.QBoard.*;

@Repository
@RequiredArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository {
    private final JPAQueryFactory query;

    public List<BoardAllResponse> findAllWithQuery(final SearchKeywordRequest searchKeyword, final Pageable pageable) {
        return query.selectFrom(board)
                .where(
                        eqTitle(searchKeyword.getTitle()),
                        eqContent(searchKeyword.getContent()),
                        eqNickname(searchKeyword.getEmail())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(BoardAllResponse::of)
                .collect(Collectors.toList());
    }

    private BooleanExpression eqTitle(final String byTitle) {
        if (!StringUtils.hasText(byTitle)) {
            return null;
        }
        return board.title.title.contains(byTitle);
    }

    private BooleanExpression eqContent(final String byContent) {
        if (!StringUtils.hasText(byContent)) {
            return null;
        }
        return board.content.content.contains(byContent);
    }

    private BooleanExpression eqNickname(final String byNickname) {
        if (!StringUtils.hasText(byNickname)) {
            return null;
        }
        return board.member.nickname.nickname.contains(byNickname);
    }
}