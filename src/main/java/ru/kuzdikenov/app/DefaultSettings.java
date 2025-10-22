package ru.kuzdikenov.app;

public class DefaultSettings {
    public static final String DB_USER = System.getenv("DB_USER");
    public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    public static final String DB_URL = System.getenv("DB_URL");
    public static final int httpSessionMaxInactiveInterval = 24 * 60 * 60;
    public static final String FILE_STORAGE_DIR = "/Users/damirkuzdikenov/Documents";
    public static final String FILE_ACCESS_URL_PATH = "/savedImages";

}
