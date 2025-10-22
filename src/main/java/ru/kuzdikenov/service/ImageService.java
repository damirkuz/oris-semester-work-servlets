package ru.kuzdikenov.service;

import java.util.UUID;

public interface ImageService {
    void save(UUID uuid, String uploaderLogin, String path);
}
