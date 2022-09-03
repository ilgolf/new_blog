package me.golf.blog.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.redisForm.BoardRedisDto;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponse implements Serializable {
    private Title title;
    private Content content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:SS")
    private LocalDateTime lastModifiedAt;
    private String createdBy;

    public static BoardResponse of(final Board board ,final String email) {
        return new BoardResponse(
                board.getTitle(),
                board.getContent(),
                board.getLastModifiedTime(),
                email);
    }
}
