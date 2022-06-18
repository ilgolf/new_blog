package me.golf.blog.domain.reply.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.reply.domain.vo.Comment;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyAllResponse {
    private String email;
    private Comment comment;
    private LocalDateTime createDate;
}
