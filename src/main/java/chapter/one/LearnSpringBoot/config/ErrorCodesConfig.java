package chapter.one.LearnSpringBoot.config;

public final class ErrorCodesConfig {

    public static final Integer DEFAULT_CODE = 1;
    public static final String DEFAULT_MESSAGE = "Unknown error occurred. Please try again later.";

    // Bad credentials
    public static final Integer BAD_CREDENTIALS_CODE = 400001;
    public static final String BAD_CREDENTIALS_MESSAGE = "Invalid credentials!";

    // User already exists
    public static final Integer USER_ALREADY_EXISTS_CODE = 400000;
    public static final String USER_ALREADY_EXISTS_MESSAGE = "Invalid credentials!";
}
