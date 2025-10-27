package ru.kuzdikenov.repository;

import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.exception.ImageNotFoundInDatabaseException;

import java.util.List;
import java.util.UUID;

public interface ImageRepository {
    void save(UUID uuid, int uploaderId, String path);
    List<Image> getAllImagesFromInitiative(Initiative initiative);
    void delete(int imageId);
    Image getByUuid(UUID uuid) throws ImageNotFoundInDatabaseException;
}
