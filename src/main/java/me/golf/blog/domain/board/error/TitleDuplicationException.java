package me.golf.blog.domain.board.error;

import me.golf.blog.global.error.exception.BusinessException;
import me.golf.blog.global.error.exception.ErrorCode;

public class TitleDuplicationException extends BusinessException {
    public TitleDuplicationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
