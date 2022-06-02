package me.golf.blog.domain.board.domain.persist;

import me.golf.blog.domain.board.dto.BoardAllResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardCustomRepository {
    List<BoardAllResponse> findAllWithQuery(final SearchKeywordRequest searchKeyword, final Pageable pageable);
}
