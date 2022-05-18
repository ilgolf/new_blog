package me.golf.blog.domain.board.error;

import me.golf.blog.global.error.exception.EntityNotFoundException;
import me.golf.blog.global.error.exception.ErrorCode;

public class BoardNotFoundException extends EntityNotFoundException {
    public BoardNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
