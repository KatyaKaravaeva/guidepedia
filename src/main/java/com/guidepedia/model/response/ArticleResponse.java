package com.guidepedia.model.response;

import com.guidepedia.model.entity.ArticleEntity;
import com.guidepedia.model.entity.CategoryEntity;
import com.guidepedia.model.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ArticleResponse {

    private Long currentUserId;
    private Long articleId;
    private ProfileResponse users;
    private CategoryEntity category;
    private String title;
    private String text;
    private String description;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean draft;
    private Boolean statusSave;
    private Integer likes;
    private Boolean statusLike;

    public ArticleResponse(ArticleEntity article, UserEntity currentUser) {
        this.currentUserId = currentUser.getId();
        this.articleId = article.getId();
        this.users = new ProfileResponse(article.getCreatedBy(), article.getCreatedBy().getSubscribers().contains(currentUser));
        this.category = article.getCategory();
        this.title = article.getTitle();
        this.text = article.getText();
        this.description = article.getDescription();
        this.image = article.getImage();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.draft = article.getDraft();
        this.statusSave = article.getSavedUsers().contains(currentUser);
        this.likes = article.getUsers().size();
        this.statusLike = article.getUsers().contains(currentUser);
    }

    public List<ArticleResponse> getListArticleResponces(List<ArticleEntity> articleEntities, UserEntity user) {
        List<ArticleResponse> articleResponses = new ArrayList<>();
        for (ArticleEntity articleEntity: articleEntities) {
            articleResponses.add(new ArticleResponse(articleEntity, user));
        }
        return articleResponses;
    }
}