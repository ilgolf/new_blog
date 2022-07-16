package me.golf.blog.domain.member.error;

import me.golf.blog.global.error.exception.BusinessException;
import me.golf.blog.global.error.exception.ErrorCode;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
