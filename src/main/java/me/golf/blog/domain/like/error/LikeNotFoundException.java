package me.golf.blog.domain.like.error;

import me.golf.blog.global.error.exception.EntityNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;

public class LikeNotFoundException extends EntityNotFoundException {
    public LikeNotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
