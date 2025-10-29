package ru.kuzdikenov.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.dto.CommentOnInitiative;
import ru.kuzdikenov.entity.*;
import ru.kuzdikenov.exception.*;
import ru.kuzdikenov.helper.InitiativeUtil;
import ru.kuzdikenov.helper.LoginPasswordUtil;
import ru.kuzdikenov.repository.*;
import ru.kuzdikenov.service.InitiativeService;
import ru.kuzdikenov.servlet.InitiativeServlet;

import java.util.ArrayList;
import java.util.List;

public class InitiativeServiceImpl implements InitiativeService {
    private final InitiativeRepository initiativeRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    private static final Logger log = LoggerFactory.getLogger(InitiativeServiceImpl.class);

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
        List<CommentOnInitiative> comments = new ArrayList<>();
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
    public Initiative getById(int initiativeId, String requesterUserLogin) throws InitiativeNotFoundInDatabaseException {
        Initiative initiative = initiativeRepository.getById(initiativeId);;


        initiative.setImages(imageRepository.getAllImagesFromInitiative(initiative));
        List<Comment> commentsFromDb = commentRepository.getAllFromInitiative(initiative);
        initiative.setComments(getCommentsOnInitiative(commentsFromDb, requesterUserLogin));
        initiative.setLikes(likeRepository.getAllFromInitiative(initiative));

        return initiative;
    }

    private List<CommentOnInitiative> getCommentsOnInitiative(List<Comment> comments, String requesterUserLogin) {
        List<CommentOnInitiative> commentsOnInitiative = new ArrayList<>();
        for (Comment comment: comments) {
            String authorUserLogin;
            try {
                authorUserLogin = userRepository.getById(comment.getAuthorUserId()).getLogin();
            } catch (UserNotFoundInDatabaseException e) {
                authorUserLogin = null; // in db always have correct user id
            }

            boolean ownedByMe = authorUserLogin.equals(requesterUserLogin);
            commentsOnInitiative.add(new CommentOnInitiative(comment.getId(), authorUserLogin, comment.getBody(), ownedByMe));
        }
        return commentsOnInitiative;
    }

    @Override
    public boolean checkUserLiked(String userLogin, int initiativeId) {
        int userId;
        try {
            userId = userRepository.getByLogin(userLogin).getId();
        } catch (UserNotFoundInDatabaseException e) {
            return false;
        }
        return likeRepository.checkUserLikedInitiative(userId, initiativeId);
    }

    @Override
    public boolean checkExists(int initiativeId) {
        return initiativeRepository.checkExists(initiativeId);
    }

    @Override
    public void like(String userLogin, int initiativeId) throws InitiativeNotFoundInDatabaseException, UserNotFoundInDatabaseException {
        if (!checkExists(initiativeId)) {
            throw new InitiativeNotFoundInDatabaseException();
        }

        int userId = userRepository.getByLogin(userLogin).getId();

        if (checkUserLiked(userLogin, initiativeId)) {
            likeRepository.delete(userId, initiativeId);
        } else {
            Like like = new Like(userId, initiativeId);
            likeRepository.save(like);
        }
    }

    @Override
    public void comment(String userLogin, int initiativeId, String body) throws InitiativeNotFoundInDatabaseException, UserNotFoundInDatabaseException {
        if (!checkExists(initiativeId)) {
            throw new InitiativeNotFoundInDatabaseException();
        }

        int userId = userRepository.getByLogin(userLogin).getId();

        Comment comment = new Comment(userId, initiativeId, body);
        commentRepository.save(comment);
    }

    @Override
    public void delete(int initiativeId) throws InitiativeNotFoundInDatabaseException {
        initiativeRepository.delete(initiativeId);
    }

    @Override
    public void edit(int initiativeId, String title, String body, List<Image> images, InitiativeStatus status) throws InitiativeNotFoundInDatabaseException, NoChangesException {
        int changesCount = 0;
        Initiative initiative = initiativeRepository.getById(initiativeId);


        log.atInfo().log("Проверяем новый заголовок инициативы на совпадение со старым");
        if (!title.equals(initiative.getTitle())) {
            initiativeRepository.changeTitle(initiative, title);
            changesCount++;
        }

        log.atInfo().log("Проверяем новое тело инициативы на совпадение со старым");
        if (!body.equals(initiative.getBody())) {
            initiativeRepository.changeBody(initiative, body);
            changesCount++;
        }

        log.atInfo().log("Проверяем новый статус инициативы на совпадение со старым");
        if (!status.equals(initiative.getStatus())) {
            initiativeRepository.changeStatus(initiative, status);
            changesCount++;
        }

        log.atInfo().log("Проверяем новые фото инициативы на совпадение со старым");
        if (!images.equals(initiative.getImages())) {
            initiativeRepository.changeImages(initiative, images);
            changesCount++;
        }

        if (changesCount == 0) {
            throw new NoChangesException();
        }
    }

}
