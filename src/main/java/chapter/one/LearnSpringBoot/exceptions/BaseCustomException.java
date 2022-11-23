package chapter.one.LearnSpringBoot.exceptions;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseCustomException extends Exception {
    private final int errorCode;
    private final String errorMsg;

    public BaseCustomException(String message) {
        super(message);
        this.errorMsg = message;
        this.errorCode = 1; // Default error code
    }

    public BaseCustomException(String message, int code) {
        super(message);
        this.errorMsg = message;
        this.errorCode = Math.max(code, 1);
    }

    @Override
    public String toString() {
        return "BaseCustomException{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                "}\n";
    }
}
