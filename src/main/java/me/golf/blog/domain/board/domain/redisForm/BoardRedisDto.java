package me.golf.blog.domain.board.domain.redisForm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BoardRedisDto {
    private String id;
    private Title title;
    private Content content;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String viewCount;

    public BoardRedisDto(final Board board) {
        this.id = board.getId().toString();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.lastModifiedAt = board.getLastModifiedTime();
        this.createdBy = board.getCreatedBy();
        this.viewCount = String.valueOf(board.getBoardCount().getViewCount());
    }
}
