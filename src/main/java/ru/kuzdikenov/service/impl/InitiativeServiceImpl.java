package ru.kuzdikenov.service.impl;

import ru.kuzdikenov.entity.*;
import ru.kuzdikenov.exception.*;
import ru.kuzdikenov.helper.InitiativeUtil;
import ru.kuzdikenov.repository.*;
import ru.kuzdikenov.service.InitiativeService;

import java.util.ArrayList;
import java.util.List;

public class InitiativeServiceImpl implements InitiativeService {
    private final InitiativeRepository initiativeRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public InitiativeServiceImpl(InitiativeRepository initiativeRepository, UserRepository userRepository, ImageRepository imageRepository, CommentRepository commentRepository, LikeRepository likeRepository) {
        this.initiativeRepository = initiativeRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }


    @Override
    public int save(String creatorLogin, String title, String body, List<Image> images) throws FailInitiativeSaveException, InvalidInitiativeTitleException {
        if (!InitiativeUtil.isValidTitle(title)) {
            throw new InvalidInitiativeTitleException();
        }

        InitiativeStatus status = InitiativeStatus.SUGGESTED;
        List<Like> likes = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        int creatorUserId = 0;
        try {
            creatorUserId = userRepository.getByLogin(creatorLogin).getId();
        } catch (UserNotFoundInDatabaseException e) {
            throw new RuntimeException(e);
        }

        Initiative initiative = new Initiative(creatorUserId, title, body, status, images, likes, comments);
        return initiativeRepository.save(initiative);
    }

    @Override
    public Initiative getById(String id) throws InitiativeNotFoundInDatabaseException {
        int initiativeId = Integer.parseInt(id);
        Initiative initiative = initiativeRepository.getById(initiativeId);;


        initiative.setImages(imageRepository.getAllImagesFromInitiative(initiative));
        initiative.setComments(commentRepository.getAllFromInitiative(initiative));
        initiative.setLikes(likeRepository.getAllFromInitiative(initiative));

        return initiative;
    }
}
