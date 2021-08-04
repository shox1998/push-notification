package uz.kibera.project.exception;

import lombok.Getter;

@Getter
public class CustomFirebaseException extends RuntimeException {
    private String message;
    public CustomFirebaseException(String localizedMessage) {
        super(localizedMessage);
        this.message = localizedMessage;

    }
}
