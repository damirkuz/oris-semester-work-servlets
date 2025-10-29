package ru.kuzdikenov.repository;

import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.InitiativeStatus;
import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.exception.FailInitiativeSaveException;
import ru.kuzdikenov.exception.InitiativeNotFoundInDatabaseException;

import java.util.List;

public interface InitiativeRepository {
    int save(Initiative initiative) throws FailInitiativeSaveException;
    List<Initiative> getAllFromUser(User user);
    void delete(int initiativeId) throws InitiativeNotFoundInDatabaseException;
    Initiative getById(int initiativeId) throws InitiativeNotFoundInDatabaseException;
    void changeTitle(Initiative initiative, String newTitle);
    void changeBody(Initiative initiative, String newBody);
    void changeStatus(Initiative initiative, InitiativeStatus initiativeStatus);
    void changeImages(Initiative initiative, List<Image> images);
    boolean checkExists(int initiativeId);
}
