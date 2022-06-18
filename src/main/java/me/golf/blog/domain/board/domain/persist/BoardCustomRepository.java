package me.golf.blog.domain.board.domain.persist;

import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.board.dto.BoardAllResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardCustomRepository {
    List<BoardAllResponse> findAllWithQuery(final SearchKeywordRequest searchKeyword, final Pageable pageable);
    Optional<Title> existByTitle(final Title title);
}
