package me.golf.blog.domain.member.error;

import me.golf.blog.global.error.exception.BusinessException;
import me.golf.blog.global.error.exception.ErrorCode;

public class PasswordNullException extends BusinessException {

    public PasswordNullException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
