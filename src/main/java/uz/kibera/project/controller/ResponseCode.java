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
    USER_NOT_FOUND(402, "User not found"),
    ILLEGAL_USER_STATUS(403, "Illegal user status"),
    USERNAME_ALREADY_EXIST(405, "This username already exist"),
    EMAIL_ALREADY_EXIST(406, "This email already exist"),
    INVALID_FILE_TYPE(407, "File must be an image"),
    FILE_IS_EMPTY(408, "File is empty"),
    NOTICE_NOT_FOUND(409, "Notice not found"),
    PUSH_NOT_FOUND(410, "Push not found"),
    IMAGE_NOT_FOUND(411, "Image not found"),
    FILE_IS_TO_LARGE(412, "File is to large"),
    BAD_CREDENTIALS(413, "Incorrect username or password"),
    USER_HAVE_NOT_ACCESS(420, "This user haven't access for do this operation"),
    FIRE_BASE_SEND_FAILED(414, "Firebase unavailable. Error :");

    private final int code;
    private final String message;
}
