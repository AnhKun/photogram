package com.anguyen.photogram.exceptions;

import com.anguyen.photogram.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        ApiResponse response = ApiResponse.builder()
                .code(errorCode.getStatusCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        ApiResponse response = ApiResponse.builder()
                .code(errorCode.getStatusCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
        ApiResponse response = ApiResponse.builder()
                .code(ErrorCode.UNCATEGORIZED.getStatusCode())
                .message(ErrorCode.UNCATEGORIZED.getMessage())
                .build();

        return ResponseEntity
                .status(ErrorCode.UNCATEGORIZED.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getFieldError().getDefaultMessage();

        ApiResponse response = ApiResponse.builder()
                .code(HttpStatus.BAD_REQUEST)
                .message(message)
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
