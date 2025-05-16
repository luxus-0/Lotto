package pl.lotto.infrastructure.secret;

public class SecretConstantsRegex {
    public static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&*!])[A-Za-z\\d@#$%^&*!]{8,}$";

    public static final String PASSWORD_MESSAGE =
            "Password must contain at least 8 characters, including uppercase, lowercase, digit, and special character (@#$%^&*!).";
}
