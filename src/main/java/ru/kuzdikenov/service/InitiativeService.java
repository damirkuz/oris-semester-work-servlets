package ru.kuzdikenov.service;

import ru.kuzdikenov.entity.*;
import ru.kuzdikenov.exception.*;

import java.util.List;

public interface InitiativeService {
    int save(String creatorLogin, String title, String body, List<Image> images) throws FailInitiativeSaveException, InvalidInitiativeTitleException;
    Initiative getById(int initiativeId, String requesterUserLogin) throws InitiativeNotFoundInDatabaseException;
    boolean checkUserLiked(String userLogin, int initiativeId);
    boolean checkExists(int initiativeId);
    void like(String userLogin, int initiativeId) throws InitiativeNotFoundInDatabaseException, UserNotFoundInDatabaseException;
    void comment(String userLogin, int initiativeId, String body) throws InitiativeNotFoundInDatabaseException, UserNotFoundInDatabaseException;
    void delete(int initiativeId) throws InitiativeNotFoundInDatabaseException;
    void edit(int initiativeId, String title, String body, List<Image> images, InitiativeStatus status) throws InitiativeNotFoundInDatabaseException, NoChangesException;
    List<Initiative> getAll();
}
