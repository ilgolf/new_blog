package me.golf.blog.domain.board.domain.redisForm;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@RedisHash("board")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardRedisEntity implements Serializable {
    private static final long serialVersionUID = 123123124L;
    @Id
    private Long id;
    private Title title;
    private Content content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private Long boardCountId;

    public BoardRedisEntity(final Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.lastModifiedAt = board.getLastModifiedTime();
        this.createdBy = board.getCreatedBy();
        this.boardCountId = board.getBoardCount().getId();
    }
}
