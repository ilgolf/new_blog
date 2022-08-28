package me.golf.blog.domain.follower.error;

import me.golf.blog.global.error.exception.EntityNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;

public class FollowNotFoundException extends EntityNotFoundException {
    public FollowNotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
