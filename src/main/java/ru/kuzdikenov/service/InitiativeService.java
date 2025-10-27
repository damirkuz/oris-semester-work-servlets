package ru.kuzdikenov.service;

import ru.kuzdikenov.entity.*;
import ru.kuzdikenov.exception.FailInitiativeSaveException;
import ru.kuzdikenov.exception.InitiativeNotFoundInDatabaseException;
import ru.kuzdikenov.exception.InvalidInitiativeTitleException;
import ru.kuzdikenov.exception.UserNotFoundInDatabaseException;

import java.util.List;

public interface InitiativeService {
    int save(String creatorLogin, String title, String body, List<Image> images) throws FailInitiativeSaveException, InvalidInitiativeTitleException;
    Initiative getById(String id) throws InitiativeNotFoundInDatabaseException;
    boolean checkUserLiked(String userLogin, int initiativeId);
    boolean checkExists(int initiativeId);
    void like(String userLogin, int initiativeId) throws InitiativeNotFoundInDatabaseException, UserNotFoundInDatabaseException;

}
