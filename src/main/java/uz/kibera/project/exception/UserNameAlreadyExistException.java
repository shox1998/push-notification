package uz.kibera.project.exception;

public class UserNameAlreadyExistException extends RuntimeException {
    public UserNameAlreadyExistException(String s) {
        super(s);
    }
}
