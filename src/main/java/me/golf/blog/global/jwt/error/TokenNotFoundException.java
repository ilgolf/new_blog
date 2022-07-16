package me.golf.blog.global.jwt.error;

import me.golf.blog.global.error.exception.BusinessException;
import me.golf.blog.global.error.exception.ErrorCode;

public class TokenNotFoundException extends BusinessException {
    public TokenNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
