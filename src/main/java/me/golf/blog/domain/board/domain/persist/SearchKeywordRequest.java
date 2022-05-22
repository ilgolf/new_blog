package me.golf.blog.domain.board.domain.persist;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class SearchKeywordRequest {
    private final String byTitle;

    private final String byContent;

    private final String byEmail;
}
