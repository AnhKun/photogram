package com.anguyen.photogram.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    IMAGE_INVALID(HttpStatus.BAD_REQUEST, "Only image files are allowed"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, ""),
    REPEAT_LOGOUT(HttpStatus.BAD_REQUEST, "You already logged out!"),
    USER_EXISTED(HttpStatus.BAD_REQUEST, "User is already taken"),
    UNCATEGORIZED(HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized error"),
    UPDATE_OTHERS(HttpStatus.BAD_REQUEST, "Cannot update other's"),
    DELETE_OTHERS(HttpStatus.BAD_REQUEST, "Cannot delete other's"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Unauthenticated"),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "You do not have permission");

    private HttpStatus statusCode;
    private String message;

    ErrorCode(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
