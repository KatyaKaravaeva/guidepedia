package com.guidepedia.model.response;

import com.guidepedia.model.entity.ArticleEntity;
import com.guidepedia.model.entity.CategoryEntity;
import com.guidepedia.model.entity.CommentEntity;
import com.guidepedia.model.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {
    private Long commentId;
    private ArticleResponse article;
    private ProfileResponse author;
    private String comment;

    public CommentResponse(CommentEntity comment, UserEntity currentUser) {
        this.commentId = comment.getId();
        this.author = new ProfileResponse(comment.getUser(), comment.getUser().getSubscribers().contains(currentUser));
        this.article = new ArticleResponse(comment.getArticle(), currentUser);
        this.comment = comment.getComment();
    }

    public List<CommentResponse> getListCommentResponces(List<CommentEntity> commentEntities, UserEntity user) {
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (CommentEntity comment: commentEntities) {
            commentResponses.add(new CommentResponse(comment, user));
        }
        return commentResponses;
    }
}