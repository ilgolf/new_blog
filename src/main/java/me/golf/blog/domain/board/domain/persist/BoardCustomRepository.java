package me.golf.blog.domain.board.domain.persist;

import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.board.dto.BoardAllResponse;
import me.golf.blog.domain.board.dto.TempBoardListResponse;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.global.common.PageCustomResponse;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardCustomRepository {
    PageCustomResponse<BoardAllResponse> findAllWithQuery(final SearchKeywordRequest searchKeyword, final Pageable pageable);
    Optional<Title> existByTitle(final Title title);
    PageCustomResponse<BoardAllResponse> findByEmail(final Email email, final Pageable pageable);
    PageCustomResponse<TempBoardListResponse> findAllTempBoard(final Long memberId, final Pageable pageable);
}
