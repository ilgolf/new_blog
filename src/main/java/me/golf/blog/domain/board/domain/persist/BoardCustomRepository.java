package me.golf.blog.domain.board.domain.persist;

import me.golf.blog.domain.board.dto.BoardAllResponse;
import me.golf.blog.domain.board.dto.BoardResponse;
import me.golf.blog.domain.board.dto.TempBoardListResponse;
import me.golf.blog.domain.member.domain.vo.Email;
import me.golf.blog.domain.member.domain.vo.Nickname;
import me.golf.blog.global.common.PageCustomResponse;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardCustomRepository {
    PageCustomResponse<BoardAllResponse> findAllWithQuery(final SearchKeywordRequest searchKeyword, final Pageable pageable);
    PageCustomResponse<BoardAllResponse> findByNickname(final Nickname nickname, final Pageable pageable);
    PageCustomResponse<TempBoardListResponse> findAllTempBoard(final Long memberId, final Pageable pageable);
    Optional<BoardResponse> getBoardDetail(final Long boardId);
}
