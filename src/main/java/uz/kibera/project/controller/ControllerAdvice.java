package uz.kibera.project.controller;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import uz.kibera.project.exception.JwtAuthenticationException;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse on(Exception e) {
        log.error(ResponseCode.GENERAL_ERROR.getMessage(), e);
        return ErrorResponse.of(ResponseCode.GENERAL_ERROR.getCode(), ResponseCode.GENERAL_ERROR.getMessage() + ": " + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse on(MethodArgumentNotValidException e) {
        Map<String, Object> meta = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            meta.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ErrorResponse.of(ResponseCode.VALIDATION_ERROR.getCode(), ResponseCode.VALIDATION_ERROR.getMessage(), meta);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse on(JwtAuthenticationException e) {
        return ErrorResponse.of(ResponseCode.INVALID_ACCESS_TOKEN.getCode(), ResponseCode.INVALID_ACCESS_TOKEN.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse on(MaxUploadSizeExceededException exception) {
        return ErrorResponse.of(ResponseCode.FILE_IS_TO_LARGE);
    }

//    @ExceptionHandler(ImageNotFoundException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    ErrorResponse on(ImageNotFoundException exception) {
//        return uz.davrbank.davrpay.auth.controllers.ErrorResponse.of(ResponseCode.IMAGE_NOT_FOUND);
//    }
//
//    @ExceptionHandler(InvalidFileSizeException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    uz.davrbank.davrpay.auth.controllers.ErrorResponse on(InvalidFileSizeException ignored) {
//        return uz.davrbank.davrpay.auth.controllers.ErrorResponse.of(ResponseCode.FILE_IS_EMPTY);
//    }
//
//    @ExceptionHandler(InvalidFileFormat.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    uz.davrbank.davrpay.auth.controllers.ErrorResponse on(InvalidFileFormat ignored) {
//        return uz.davrbank.davrpay.auth.controllers.ErrorResponse.of(ResponseCode.INVALID_FILE_TYPE);
//    }
//
//    @ExceptionHandler(InvalidConfirmPassword.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    uz.davrbank.davrpay.auth.controllers.ErrorResponse on(InvalidConfirmPassword ignored) {
//        return uz.davrbank.davrpay.auth.controllers.ErrorResponse.of(ResponseCode.INVALID_CONFIRM_PASSWORD);
//    }

//    @ExceptionHandler(InvalidUserPassword.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    uz.davrbank.davrpay.auth.controllers.ErrorResponse on(InvalidUserPassword ignored) {
//        return uz.davrbank.davrpay.auth.controllers.ErrorResponse.of(ResponseCode.INVALID_USER_PASSWORD);
//    }

//    @ExceptionHandler(UserNotFoundException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    ErrorResponse on(UserNotFoundException ignored) {
//        return ErrorResponse.of(ResponseCode.USER_NOT_FOUND);
//    }
//
//    @ExceptionHandler(InvalidUserStateException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    ErrorResponse on(InvalidUserStateException ignored) {
//        return ErrorResponse.of(ResponseCode.ILLEGAL_USER_STATUS);
//    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse on(ConstraintViolationException e) {
        Map<String, List<String>> constraintViolationMap = new HashMap<>();
        for (ConstraintViolation<?> fieldError : e.getConstraintViolations()) {
            String field = StreamSupport
                    .stream(fieldError.getPropertyPath().spliterator(), false)
                    .reduce((node, node2) -> node2).map(Path.Node::toString).orElse("");

            field = field.isBlank() ? "general_errors" : field;
            constraintViolationMap
                    .computeIfAbsent(field, s -> new ArrayList<>())
                    .add(fieldError.getMessage());
        }

        return ErrorResponse.of(
                ResponseCode.VALIDATION_ERROR,
                new HashMap<>(constraintViolationMap)
        );
    }
}
