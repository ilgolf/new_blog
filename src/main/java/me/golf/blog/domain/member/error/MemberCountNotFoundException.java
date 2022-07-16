package me.golf.blog.domain.member.error;

import me.golf.blog.global.error.exception.EntityNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;

public class MemberCountNotFoundException extends EntityNotFoundException {
    public MemberCountNotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
