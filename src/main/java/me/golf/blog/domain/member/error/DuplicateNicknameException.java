package me.golf.blog.domain.member.error;

import me.golf.blog.global.error.exception.BusinessException;
import me.golf.blog.global.error.exception.ErrorCode;

public class DuplicateNicknameException extends BusinessException {
    public DuplicateNicknameException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
