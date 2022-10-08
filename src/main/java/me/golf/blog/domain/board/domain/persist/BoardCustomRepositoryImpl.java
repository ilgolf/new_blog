package me.golf.blog.domain.board.domain.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.golf.blog.domain.board.domain.vo.BoardStatus;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.board.dto.BoardAllResponse;
import me.golf.blog.domain.board.dto.BoardResponse;
import me.golf.blog.domain.board.dto.TempBoardListResponse;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.global.common.PageCustomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static me.golf.blog.domain.board.domain.persist.QBoard.*;
import static me.golf.blog.domain.board.domain.persist.express.BoardExpression.*;
import static me.golf.blog.domain.member.domain.persist.QMember.member;

@Repository
@RequiredArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository {
    private final JPAQueryFactory query;

    public PageCustomResponse<BoardAllResponse> findAllWithQuery(final SearchKeywordRequest searchKeyword, final Pageable pageable) {
        List<BoardAllResponse> boards = query.select(Projections.constructor(BoardAllResponse.class,
                        board.id,
                        board.title,
                        board.content,
                        member.email.as("createdBy"),
                        board.createTime.as("createdAt"))
                )
                .from(board)
                .innerJoin(member).on(member.id.eq(board.memberId))
                .where(
                        EQ_TITLE.eqBoardField(searchKeyword.getTitle()),
                        EQ_CONTENT.eqBoardField(searchKeyword.getContent()),
                        EQ_NICKNAME.eqBoardField(searchKeyword.getNickname())
                )
                .where(board.status.eq(BoardStatus.SAVE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetch();

        return getPageResponse(pageable, boards);
    }

    public PageCustomResponse<BoardAllResponse> findByEmail(Email email, Pageable pageable) {
        List<BoardAllResponse> boards = query.select(Projections.constructor(BoardAllResponse.class,
                        board.id,
                        board.title,
                        board.content,
                        member.email,
                        board.createTime.as("createdAt"))
                )
                .from(board)
                .innerJoin(member).on(member.id.eq(board.memberId))
                .where(board.status.eq(BoardStatus.SAVE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetch();

        return getPageResponse(pageable, boards);
    }

    public PageCustomResponse<TempBoardListResponse> findAllTempBoard(Long memberId, Pageable pageable) {
        List<TempBoardListResponse> boards = query.select(Projections.constructor(TempBoardListResponse.class,
                        board.title,
                        board.content)
                )
                .from(board)
                .where(board.status.eq(BoardStatus.TEMP))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetch();

        if (boards.size() == 0) {
            return PageCustomResponse.of(Page.empty());
        }

        JPAQuery<Long> count = query.select(board.count())
                .from(board)
                .where(board.status.eq(BoardStatus.TEMP));

        return PageCustomResponse.of(PageableExecutionUtils.getPage(boards, pageable, count::fetchFirst));
    }

    public Optional<BoardResponse> getBoardDetail(final Long boardId) {

        return Optional.ofNullable(query.select(Projections.constructor(BoardResponse.class,
                        board.title,
                        board.content,
                        board.lastModifiedTime.as("lastModifiedAt"),
                        member.nickname))
                .from(board)
                .innerJoin(member).on(member.id.eq(board.memberId))
                .where(board.id.eq(boardId))
                .fetchFirst());
    }

    private PageCustomResponse<BoardAllResponse> getPageResponse(Pageable pageable, List<BoardAllResponse> boards) {
        if (boards.size() == 0) {
            return PageCustomResponse.of(Page.empty());
        }

        JPAQuery<Long> count = query.select(board.count())
                .from(board)
                .where(board.status.eq(BoardStatus.SAVE));

        return PageCustomResponse.of(PageableExecutionUtils.getPage(boards, pageable, count::fetchFirst));
    }
}
