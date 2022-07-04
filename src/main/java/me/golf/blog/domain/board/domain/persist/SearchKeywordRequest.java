package me.golf.blog.domain.board.domain.persist;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class SearchKeywordRequest {
    private String title;
    private String content;
    private String nickname;
}
