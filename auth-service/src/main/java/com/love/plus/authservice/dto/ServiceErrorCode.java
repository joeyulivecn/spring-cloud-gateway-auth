package com.love.plus.authservice.dto;

public enum ServiceErrorCode {

    UNEXPECTED_ERROR(1, "Unexpected error."),

    // Http 4xx - 5xx
    UNAUTHORIZED(401, "401 Unauthorized"),

    // Customized 1001 - 1999
    INVLIAD_TOKEN(10001, "Invalid token"),
    USERNAME_EXIST(10001, "A user with the same username already exists."),
    INVALID_CREDENTIAL(2002, "Invalid username or password.");


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private final int code;
    private final String message;

    ServiceErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
