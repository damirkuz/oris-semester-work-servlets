package ru.kuzdikenov.repository.dao;

import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.entity.UserRole;

public interface ImageDao {
    void save(Image image);
    Image getById(int imageId);
    void delete(int imageId);
}
