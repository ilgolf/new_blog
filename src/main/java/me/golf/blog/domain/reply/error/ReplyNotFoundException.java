package me.golf.blog.domain.reply.error;

import me.golf.blog.global.error.exception.EntityNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;

public class ReplyNotFoundException extends EntityNotFoundException {
    public ReplyNotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
