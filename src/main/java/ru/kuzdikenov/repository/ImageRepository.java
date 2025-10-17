package ru.kuzdikenov.repository;

import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.entity.Initiative;

import java.util.List;

public interface ImageRepository {
    void save(Image image);
    List<Image> getAllImagesFromInitiative(Initiative initiative);
    void delete(int imageId);

}
