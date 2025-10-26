package ru.kuzdikenov.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.app.DefaultSettings;
import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.exception.ImageNotFoundInDatabaseException;
import ru.kuzdikenov.exception.UserNotFoundInDatabaseException;
import ru.kuzdikenov.helper.ImageUtil;
import ru.kuzdikenov.repository.ImageRepository;
import ru.kuzdikenov.repository.UserRepository;
import ru.kuzdikenov.service.ImageService;

import java.util.UUID;

public class ImageServiceImpl implements ImageService {
    public static final String FILE_PREFIX = DefaultSettings.FILE_STORAGE_DIR;
    public static final int DIRECTORIES_COUNT = 100;
    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    public ImageServiceImpl(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void save(UUID uuid, String uploaderLogin, String path) {
        int uploaderId;
        try {
            uploaderId = userRepository.getByLogin(uploaderLogin).getId();
        } catch (UserNotFoundInDatabaseException e) {
            throw new RuntimeException(e);
        }

        imageRepository.save(uuid, uploaderId, path);
    }

    @Override
    public Image getByUuid(UUID uuid) {
        try {
            return imageRepository.getByUuid(uuid);
        } catch (ImageNotFoundInDatabaseException e) {
            throw new RuntimeException(e);
        }
    }


}
