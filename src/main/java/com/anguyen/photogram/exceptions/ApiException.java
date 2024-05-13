package com.anguyen.photogram.exceptions;

import java.util.Arrays;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public ApiException(ErrorCode errorCode, String... obj) {
        String newMessage = errorCode.getMessage();

        if (Arrays.stream(obj).count() != 0) {
            newMessage = String.format("%s", Arrays.stream(obj).findFirst().get());
        }
        this.message = newMessage;
        this.errorCode = errorCode;
    }
}
