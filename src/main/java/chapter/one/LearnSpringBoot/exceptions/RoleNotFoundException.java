package chapter.one.LearnSpringBoot.exceptions;

public class RoleNotFoundException extends BaseCustomException {

    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, int code) {
        super(message, code);
    }
}
