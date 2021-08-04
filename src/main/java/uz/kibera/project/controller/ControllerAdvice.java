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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import uz.kibera.project.exception.*;

import static uz.kibera.project.controller.ResponseCode.FIRE_BASE_SEND_FAILED;
import static uz.kibera.project.controller.ResponseCode.USERNAME_ALREADY_EXIST;

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
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ErrorResponse on(BadCredentialsException exception) {
        return ErrorResponse.of(ResponseCode.BAD_CREDENTIALS);
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

    @ExceptionHandler(UserNameAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse on(UserNameAlreadyExistException ex) {
        return ErrorResponse.of(USERNAME_ALREADY_EXIST);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse on(EmailAlreadyExistException exception) {
        return ErrorResponse.of(ResponseCode.EMAIL_ALREADY_EXIST);
    }

    @ExceptionHandler(EmptyFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse on(EmptyFileException ignored) {
        return ErrorResponse.of(ResponseCode.FILE_IS_EMPTY);
    }

    @ExceptionHandler(NoticeNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse on(NoticeNotFoundException ignored) {
        return ErrorResponse.of(ResponseCode.NOTICE_NOT_FOUND);
    }

    @ExceptionHandler(PushNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse on(PushNotFoundException ignored) {
        return ErrorResponse.of(ResponseCode.PUSH_NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse on(UserNotFoundException ignored) {
        return ErrorResponse.of(ResponseCode.USER_NOT_FOUND);
    }

    @ExceptionHandler(CustomFirebaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse on(CustomFirebaseException exception) {
        return ErrorResponse.of(FIRE_BASE_SEND_FAILED.getCode(), FIRE_BASE_SEND_FAILED.getMessage() + exception.getMessage());
    }
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
