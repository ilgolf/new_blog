package me.golf.blog.global.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.global.error.exception.ErrorCode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private int status;
    private String code;
    private List<CustomFieldError> errors;

    private ErrorResponse(final ErrorCode errorCode) {
        message = errorCode.getMessage();
        status = errorCode.getStatus();
        code = errorCode.getCode();
        errors = new ArrayList<>();
    }

    private ErrorResponse(final ErrorCode code, final List<CustomFieldError> errors) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.code = code.getCode();
    }

    public static ErrorResponse createBinding(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, CustomFieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class CustomFieldError {
        private String field;
        private String value;
        private String reason;

        private CustomFieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<CustomFieldError> of(final String field, final String value, final String reason) {
            List<CustomFieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new CustomFieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<CustomFieldError> of(final BindingResult bindingResult) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new CustomFieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}