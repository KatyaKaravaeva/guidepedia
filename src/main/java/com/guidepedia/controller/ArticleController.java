package com.guidepedia.controller;

import com.guidepedia.model.entity.ArticleEntity;
import com.guidepedia.model.entity.CategoryEntity;
import com.guidepedia.model.entity.CommentEntity;
import com.guidepedia.model.request.ArticleRequest;
import com.guidepedia.model.request.CommentRequest;
import com.guidepedia.model.request.ReactionRequest;
import com.guidepedia.model.response.ArticleResponse;
import com.guidepedia.model.response.CommentResponse;
import com.guidepedia.model.response.ProfileResponse;
import com.guidepedia.security.services.UserDetailsImpl;
import com.guidepedia.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(description = "Api to manage articles",
        name = "Article Resource")
@CrossOrigin
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Operation(summary = "Create article",
            description = "Create article")
    @PostMapping(value = "/user/article")
    @ResponseBody
    public ArticleResponse createArticle(@RequestBody ArticleRequest articleRequest, @AuthenticationPrincipal UserDetailsImpl user){
        return articleService.createArticle(articleRequest, user);
    }

    @Operation(summary = "Update article",
            description = "Update article")
    @PutMapping(value = "/user/article/{articleId}")
    @ResponseBody
    public ArticleResponse updateArticle(@RequestBody ArticleRequest articleRequest, @AuthenticationPrincipal UserDetailsImpl user, @RequestParam Long articleId){
        return articleService.updateArticle(articleRequest, user, articleId);
    }

    @Operation(summary = "Get article by Article Id",
            description = "Get article by Article Id")
    @GetMapping(value = "/user/article/{articleId}")
    @ResponseBody
    @Cacheable("articleById")
    public ArticleResponse getArticleById(@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl user){
        return articleService.getArticleById(articleId, user);
    }

    @Operation(summary = "Get article by Category Id",
            description = "Get article by Category Id")
    @GetMapping(value = "/user/article/category/{categoryId}")
    @ResponseBody
    @Cacheable("articleByCategoryId")
    public List<ArticleResponse> getArticleByCategoryId(@PathVariable Integer categoryId, @AuthenticationPrincipal UserDetailsImpl user){
        return articleService.getArticleByCategoryId(categoryId, user);
    }

    @Operation(summary = "Get current user's articles",
            description = "Get current user's articles")
    @GetMapping(value = "/user/article")
    @ResponseBody
    @Cacheable("article")
    public List<ArticleResponse> getArticle(@AuthenticationPrincipal UserDetailsImpl user){
        return articleService.getUserArticle(user);
    }

    @Operation(summary = "Get user's articles by UserId",
            description = "Get user's articles by UserId")
    @GetMapping(value = "/user/articles/{userId}")
    @ResponseBody
    @Cacheable("articlesByUserId")
    public List<ArticleResponse> getArticles(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable Long userId){
        return articleService.getUserArticles(user, userId);
    }

    @Operation(summary = "Get current user's article's drafts",
            description = "Get current user's article's drafts")
    @GetMapping(value = "/user/article/draft")
    @ResponseBody
    public List<ArticleResponse> getArticleDrafts(@AuthenticationPrincipal UserDetailsImpl user){
        return articleService.getUserArticleDrafts(user);
    }

    @Operation(summary = "Get all articles",
            description = "Get all articles")
    @GetMapping(value = "/article")
    @ResponseBody

    public List<ArticleResponse> getAllArticle(@AuthenticationPrincipal UserDetailsImpl user){
        return articleService.getAllArticle(user);
    }

    @Operation(summary = "Get all categories",
            description = "Get all categories")
    @GetMapping(value = "/article/category")
    @ResponseBody
    @Cacheable("category")
    public List<CategoryEntity> getAllCategories(){
        return articleService.getAllCategories();
    }

    @Operation(summary = "Create reaction",
            description = "Create reaction")
    @PostMapping(value = "/user/article/{articleId}/reaction")
    @ResponseBody
    public ArticleResponse createReaction(@PathVariable Long articleId, @RequestParam("reaction") Boolean reaction, @AuthenticationPrincipal UserDetailsImpl user){
        return articleService.createReaction(articleId, reaction, user);
    }

    @Operation(summary = "Get count of reactions of article",
            description = "Get count of reactions of article")
    @GetMapping(value = "/user/article/reaction/count/{articleId}")
    @ResponseBody
    public Integer getCountReactions(@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl user){
        return articleService.getCountReactions(articleId, user);
    }

    @Operation(summary = "Get articles of user's subscribtions",
            description = "Get articles of user's subscribtion")
    @GetMapping("/user/subscribtion/article")
    @Cacheable("articlesSub")
    public List<ArticleResponse> getSubscribtionArticles(@AuthenticationPrincipal UserDetailsImpl user) {
        return articleService.getSubscribtionArticles(user);
    }

    @Operation(summary = "Create comment",
            description = "Create comment")
    @PostMapping(value = "/user/article/{articleId}/comment")
    @ResponseBody
    public CommentResponse createComment(@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl user,
                                         @RequestBody CommentRequest commentRequest){
        return articleService.createComment(articleId, user, commentRequest);
    }

    @Operation(summary = "Get all comments for article",
            description = "Get all comments for article")
    @GetMapping(value = "/user/article/{articleId}/comment")
    @ResponseBody
    @Cacheable("comments")
    public List<CommentResponse> getAllComments(@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl user){
        return articleService.getAllComments(articleId, user);
    }

    @Operation(summary = "Get article by word",
            description = "Search")
    @GetMapping(value = "/article/search")
    @ResponseBody
    @Cacheable("articlesearch")
    public List<ArticleResponse> getSearchArticle(@RequestParam("line") String line, @AuthenticationPrincipal UserDetailsImpl user){
        return articleService.getSearchArticle(line, user);
    }
}