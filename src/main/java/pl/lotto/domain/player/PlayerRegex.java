package pl.lotto.domain.player;

class PlayerRegex {
    static String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#!^&*])[A-Za-z\\d@#!^&*]{8,}$";
    static String PASSWORD_MESSAGE_REGEX = "Password must contain at least one uppercase letter, one digit, one special character (@#!^&*), and be at least 8 characters long.";
}
