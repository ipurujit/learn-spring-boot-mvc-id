package chapter.one.LearnSpringBoot.exceptions;

public class UserAlreadyExistsException extends BaseCustomException {


    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, int code) {
        super(message, code);
    }

}
