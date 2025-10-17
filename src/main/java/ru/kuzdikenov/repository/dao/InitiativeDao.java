package ru.kuzdikenov.repository.dao;

import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.InitiativeStatus;

import java.util.List;

public interface InitiativeDao {
    void save(Initiative initiative);
    Initiative getById(int initiativeId);
    List<Initiative> getAllFromUserId(int userId);
    void changeTitle(int initiativeId, String newTitle);
    void changeBody(int initiativeId, String newBody);
    void changeStatus(int initiativeId, InitiativeStatus initiativeStatus);
    void delete(int initiativeId);
}
