package me.golf.blog.domain.member.error;

import me.golf.blog.global.error.exception.BusinessException;
import me.golf.blog.global.error.exception.ErrorCode;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
