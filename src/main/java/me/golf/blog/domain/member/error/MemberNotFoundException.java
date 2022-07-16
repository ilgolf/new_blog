package me.golf.blog.domain.member.error;

import me.golf.blog.global.error.exception.EntityNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;

public class MemberNotFoundException extends EntityNotFoundException {
    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
