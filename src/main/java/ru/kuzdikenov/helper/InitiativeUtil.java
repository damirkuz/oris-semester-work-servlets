package ru.kuzdikenov.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitiativeUtil {

    private static final Logger log = LoggerFactory.getLogger(InitiativeUtil.class);
    public static final int MIN_TITLE_LENGTH = 5;
    public static final int MAX_TITLE_LENGTH = 120;

    public static boolean isValidTitle(String title) {
        log.atInfo().log("Проверяю заголовок " + title);
        if (title.length() < MIN_TITLE_LENGTH || title.length() > MAX_TITLE_LENGTH) {
            log.atError().log("Заголовок " + title + "некорректной длины");
            return false;
        }
        return true;
    }
}
