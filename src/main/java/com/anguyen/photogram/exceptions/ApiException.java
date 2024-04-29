package com.anguyen.photogram.exceptions;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class ApiException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public ApiException(ErrorCode errorCode, String... obj) {
        String newMessage = errorCode.getMessage();

        if (Arrays.stream(obj).count() != 0) {
            newMessage = String.format("%s", Arrays.stream(obj).findFirst().get());
        }
//        super(errorCode.getMessage());
        this.message = newMessage;
        this.errorCode = errorCode;
    }
}
