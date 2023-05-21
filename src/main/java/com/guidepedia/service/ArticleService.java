package com.guidepedia.service;

import com.guidepedia.exception.BusinessException;
import com.guidepedia.exception.ErrorMessage;
import com.guidepedia.exception.MyEntityNotFoundException;
import com.guidepedia.model.entity.ArticleEntity;
import com.guidepedia.model.entity.CategoryEntity;
import com.guidepedia.model.entity.CommentEntity;
import com.guidepedia.model.entity.UserEntity;
import com.guidepedia.model.request.ArticleRequest;
import com.guidepedia.model.request.CommentRequest;
import com.guidepedia.model.response.ArticleResponse;
import com.guidepedia.model.response.CommentResponse;
import com.guidepedia.model.response.ProfileResponse;
import com.guidepedia.repo.ArticleRepository;
import com.guidepedia.repo.CategoryRepository;
import com.guidepedia.repo.CommentRepository;
import com.guidepedia.repo.UserRepository;
import com.guidepedia.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserService userService;

    @Transactional
    @CachePut(value = "articles")
    public ArticleResponse createArticle(ArticleRequest articleRequest, UserDetailsImpl user) {
        ArticleEntity articleEntity = new ArticleEntity();
        UserEntity userEntity = userService.getUser(user);
        articleEntity.setCreatedBy(userEntity);
        articleEntity.setCategory(categoryRepository.findByName(articleRequest.getCategoryName())
                .orElseThrow(() -> new MyEntityNotFoundException(articleRequest.getCategoryName())));
        articleEntity.setTitle(articleRequest.getTitle());
        articleEntity.setText(articleRequest.getText());
        articleEntity.setDescription(articleRequest.getDescription());
        articleEntity.setCreatedAt(LocalDateTime.now());
        articleEntity.setDraft(articleRequest.getDraft());
        articleEntity.setUsers(new HashSet<>());
        articleEntity.setDraft(articleRequest.getDraft());
        return new ArticleResponse(articleRepository.save(articleEntity), userEntity);
    }

    public ArticleResponse getArticleById(Long articleId, UserDetailsImpl user) {
        ArticleEntity article = articleRepository.findById(articleId)
                .orElseThrow(() -> new MyEntityNotFoundException(articleId));
        return new ArticleResponse(article, userService.getUser(user));
    }

    public List<ArticleResponse> getUserArticle(UserDetailsImpl user) {
        UserEntity userEntity = userService.getUser(user);
        ArticleResponse articleResponse = new ArticleResponse();
        return articleResponse.getListArticleResponces(articleRepository.findAllByCreatedByOrderByCreatedAtDesc(userEntity), userEntity);
    }

    public List<ArticleResponse> getUserArticles(UserDetailsImpl user, Long userId) {
        // UserEntity userEntity = userRepository.findById(userId)
        //       .orElseThrow(() -> new MyEntityNotFoundException(userId));
        //UserEntity userEntity = userService.getUser(user);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new MyEntityNotFoundException(userId));
        ArticleResponse articleResponse = new ArticleResponse();
        return articleResponse.getListArticleResponces(articleRepository.findAllByCreatedByOrderByCreatedAtDesc(userEntity), userEntity);
    }

    public List<ArticleResponse> getArticleByCategoryId(Integer categoryId, UserDetailsImpl user) {
        UserEntity userEntity = userService.getUser(user);
        if (!categoryRepository.existsById(categoryId)) {
            throw new MyEntityNotFoundException(categoryId.longValue());
        }
        ArticleResponse articleResponse = new ArticleResponse();
        return articleResponse.getListArticleResponces((articleRepository.findAllByCategoryIdOrderByCreatedAtDesc(categoryId)), userEntity);
    }

    public List<ArticleResponse> getArticleByUserId(UserEntity user, UserEntity currentUser) {
        ArticleResponse articleResponse = new ArticleResponse();
        return articleResponse.getListArticleResponces((articleRepository.findAllByCreatedByOrderByCreatedAtDesc(user)), currentUser);
    }


    public List<ArticleResponse> getAllArticle(UserDetailsImpl user) {
        UserEntity userEntity = userService.getUser(user);
        ArticleResponse articleResponse = new ArticleResponse();
        System.out.println("cach not working");
        return articleResponse.getListArticleResponces((articleRepository.findAll()), userEntity);
    }

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<ArticleResponse> getUserArticleDrafts(UserDetailsImpl user) {
        ArticleResponse articleResponse = new ArticleResponse();
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new MyEntityNotFoundException(user.getId()));
        return articleResponse.getListArticleResponces(
                (articleRepository.findAllByCreatedByAndDraftOrderByCreatedAtDesc(userEntity, true)), userEntity);
    }

    @Transactional
    public ArticleResponse createReaction(Long articleId, Boolean reaction, UserDetailsImpl user) {
        ArticleEntity article = articleRepository.findById(articleId).orElseThrow(() -> new MyEntityNotFoundException(articleId));
        UserEntity userEntity = userService.getUser(user);

        boolean contains = article.getUsers().contains(userEntity);
        if (contains && !reaction) {
            article.getUsers().remove(userEntity);
            userEntity.getArticlesReaction().remove(article);
        } else if (!contains && reaction) {
            article.getUsers().add(userEntity);
            userEntity.getArticlesReaction().add(article);
        } else {
            throw new BusinessException("Object already exist or deleted");
        }
        articleRepository.save(article);
        return new ArticleResponse(article, userEntity);
    }

    public List<ArticleResponse> getSubscribtionArticles(UserDetailsImpl user) {
        UserEntity userEntity = userService.getUser(user);
        return userEntity.getSubscriptions().stream()
                .flatMap(s -> getArticleByUserId(s, userEntity).stream())
                .distinct().collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse createComment(Long articleId, UserDetailsImpl user, CommentRequest commentRequest) {
        CommentEntity comment = new CommentEntity();
        UserEntity userEntity = userService.getUser(user);
        comment.setArticle(articleRepository.findById(articleId)
                .orElseThrow(() -> new MyEntityNotFoundException(articleId)));
        comment.setUser(userEntity);
        comment.setComment(commentRequest.getComment());
        commentRepository.save(comment);
        return new CommentResponse(comment, userEntity);
    }

    public List<CommentResponse> getAllComments(Long articleId, UserDetailsImpl user) {
        CommentResponse commentResponse = new CommentResponse();
        UserEntity userEntity = userService.getUser(user);
        return commentResponse.getListCommentResponces(commentRepository.findByArticle(articleRepository.findById(articleId)
                .orElseThrow(() -> new MyEntityNotFoundException(articleId))), userEntity);
    }

    public List<ArticleResponse> getSearchArticle(String line, UserDetailsImpl user) {
        UserEntity userEntity = userService.getUser(user);
        ArticleResponse articleResponse = new ArticleResponse();
        System.out.println(line.toLowerCase());
        System.out.println(articleRepository.findByTitleContainingIgnoreCase(line));
        return articleResponse.getListArticleResponces(articleRepository.findByTitleContainingIgnoreCase(line), userEntity);
    }

    public Integer getCountReactions(Long articleId, UserDetailsImpl user) {
        UserEntity userEntity = userService.getUser(user);
        ArticleResponse articleResponse = new ArticleResponse(articleRepository.findById(articleId).orElseThrow(() -> new MyEntityNotFoundException(articleId)), userEntity);
        return articleResponse.getLikes();
    }

    @Transactional
    public ArticleResponse updateArticle(ArticleRequest articleRequest, UserDetailsImpl user, Long articleId) {
        ArticleEntity article = articleRepository.findById(articleId)
                .orElseThrow(() -> new MyEntityNotFoundException(articleId));
        article.setCategory(categoryRepository.findByName(articleRequest.getCategoryName())
                .orElseThrow(() -> new MyEntityNotFoundException(articleRequest.getCategoryName())));
        article.setDraft(articleRequest.getDraft());
        article.setDescription(articleRequest.getDescription());
        article.setTitle(articleRequest.getTitle());
        article.setText(articleRequest.getText());
        article.setImage(articleRequest.getImage());
        articleRepository.save(article);
        return new ArticleResponse(article, userService.getUser(user));
    }
}