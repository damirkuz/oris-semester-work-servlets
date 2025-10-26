package ru.kuzdikenov.service;

import ru.kuzdikenov.entity.Image;

import java.util.UUID;

public interface ImageService {
    void save(UUID uuid, String uploaderLogin, String path);
    Image getByUuid(UUID uuid);
}
