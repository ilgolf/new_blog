package me.golf.blog.domain.board.domain.persist;

import me.golf.blog.domain.board.domain.redisForm.BoardRedisEntity;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.board.dto.BoardAllResponse;
import me.golf.blog.domain.board.dto.LikeAllResponse;
import me.golf.blog.domain.board.dto.TempBoardListResponse;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.global.common.PageCustomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface BoardCustomRepository {
    PageCustomResponse<BoardAllResponse> findAllWithQuery(final SearchKeywordRequest searchKeyword, final Pageable pageable);
    Optional<Title> existByTitle(final Title title);
    PageCustomResponse<BoardAllResponse> findByEmail(final Email email, final Pageable pageable);
    PageCustomResponse<TempBoardListResponse> findAllTempBoard(final Long memberId, final Pageable pageable);
    Optional<BoardRedisEntity> findRedisEntity(final Long boardId); // Redis에 데이터가 없을 때 DB에서 가져온다. (오류 대비)
}
