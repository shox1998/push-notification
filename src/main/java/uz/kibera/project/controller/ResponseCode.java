package uz.kibera.project.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    GENERAL_ERROR(500, "General error"),
    VALIDATION_ERROR(499, "Validation error"),
    BLOCKED_ACCOUNT(495, "Your account has been blocked due to suspicious activity, please contact support."),
    INVALID_ACCESS_TOKEN(401, "JWT token is expired or invalid"),
    USER_NOT_FOUND(402, "Username not found"),
    ILLEGAL_USER_STATUS(403, "Illegal user status"),
    USERNAME_ALREADY_EXIST(405, "This username already exist"),
    INVALID_FILE_TYPE(1010, "File must be an image"),
    FILE_IS_EMPTY(1011, "File is empty"),
    FILE_IS_TO_LARGE(1012, "File is to large"),
    IMAGE_NOT_FOUND(1013, "Image not found"),

    ;
    private final int code;
    private final String message;
}
