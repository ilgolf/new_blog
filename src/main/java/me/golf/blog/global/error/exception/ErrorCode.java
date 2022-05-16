package me.golf.blog.global.error.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    METHOD_NOT_ALLOWED(405, "C001", "잘못된 요청입니다."),
    HANDLE_ACCESS_DENIED(400, "c002", "잘못된 접근입니다."),
    INVALID_INPUT_VALUE(400, "C003", "잘못된 입력입니다."),
    ENTITY_NOT_FOUND(400, "C004", "개체를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C005", "서버 내부 오류 입니다."),
    INVALID_TYPE_VALUE(400, "C006", "잘못된 타입 입니다."),

    // Member
    PASSWORD_NULL_ERROR(400, "M001", "비밀번호가 없습니다."),
    USER_NOT_FOUND(400, "M002", "없는 회원 입니다."),

    // JWT
    TOKEN_NOT_FOUND(400, "J001", "잘못된 토큰입니다."),

    // Board
    BOARD_NOT_FOUND(400, "B001", "해당 게시판을 찾을 수 없습니다."),
    BOARD_MISS_MATCH(400, "B002", "해당 회원의 게시판이 아닙니다");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
