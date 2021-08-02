package uz.kibera.project.controller;

import java.util.Map;

import lombok.Value;
import org.springframework.lang.Nullable;

@Value(staticConstructor = "of")
public class ErrorResponse {

    int code;
    String message;

    @Nullable
    Map<String, Object> meta;

    public static ErrorResponse of(int code, String msg) {
        return of(code, msg, null);
    }

    public static ErrorResponse of(ResponseCode responseCode, Map<String, Object> meta) {
        return of(responseCode.getCode(), responseCode.getMessage(), meta);
    }

    public static ErrorResponse of(ResponseCode responseCode) {
        return of(responseCode.getCode(), responseCode.getMessage());
    }

    public static ErrorResponse of(ResponseCode responseCode, Object... params) {
        return of(responseCode.getCode(), String.format(responseCode.getMessage(), params));
    }
}
