package ru.kuzdikenov.repository;

import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.InitiativeStatus;
import ru.kuzdikenov.entity.User;

import java.util.List;

public interface InitiativeRepository {
    void save(Initiative initiative);
    List<Initiative> getAllFromUser(User user);
    void delete(Initiative initiative);
    void edit(Initiative initiative);
}
