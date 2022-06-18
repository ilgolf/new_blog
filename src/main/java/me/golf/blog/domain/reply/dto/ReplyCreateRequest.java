package me.golf.blog.domain.reply.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.reply.domain.vo.Comment;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ReplyCreateRequest {
    @Valid
    @NotNull(message = "필수 값입니다. - replyContent")
    private Comment comment;
}
