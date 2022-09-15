package me.golf.blog.domain.follower.error;

import me.golf.blog.global.error.exception.ErrorCode;
import me.golf.blog.global.error.exception.InvalidValueException;

public class NotSameFollowerId extends InvalidValueException {
    public NotSameFollowerId(final ErrorCode errorCode) {
        super(errorCode);
    }
}
