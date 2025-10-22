package ru.kuzdikenov.helper;

import jakarta.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginPasswordUtil {

    private static final Logger log = LoggerFactory.getLogger(LoginPasswordUtil.class);

    public static final int MIN_PASSWORD_LENGTH = 4;
    public static final int MAX_PASSWORD_LENGTH = 50;

    public static final int MIN_LOGIN_LENGTH = 5;
    public static final int MAX_LOGIN_LENGTH = 30;

    // запрещает создание экземпляров класса
    private LoginPasswordUtil() {}

    public static String encrypt(String password) {
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean checkStringNotContainsBannedSymbols(String input) {
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (!(ch >= '!' && ch <= '~')) {
                log.atError().log("В строке " + input + "обнаружен некорректный символ: " + ch);
                return false;
            }
        }
        log.atInfo().log("Строка " + input + " валидна");
        return true;
    }

    public static boolean isValidPassword(String password) {
        log.atInfo().log("Проверяю пароль " + password);
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            log.atError().log("Пароль" + password + "некорректной длины");
            return false;
        }

        return checkStringNotContainsBannedSymbols(password);
    }

    public static boolean isValidLogin(String login) {
        log.atInfo().log("Проверяю логин " + login);
        if (login.length() < MIN_LOGIN_LENGTH || login.length() > MAX_LOGIN_LENGTH) {
            log.atError().log("Логин" + login + "некорректной длины");
            return false;
        }

        return checkStringNotContainsBannedSymbols(login);
    }

}
