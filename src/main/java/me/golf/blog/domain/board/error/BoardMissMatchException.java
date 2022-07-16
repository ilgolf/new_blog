package me.golf.blog.domain.board.error;

import me.golf.blog.global.error.exception.ErrorCode;
import me.golf.blog.global.error.exception.InvalidValueException;

public class BoardMissMatchException extends InvalidValueException {
    public BoardMissMatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
