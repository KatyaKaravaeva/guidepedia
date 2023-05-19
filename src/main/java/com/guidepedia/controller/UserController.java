package com.guidepedia.controller;

import com.guidepedia.model.request.AuthRequest;
import com.guidepedia.model.response.ArticleResponse;
import com.guidepedia.model.response.JwtResponse;
import com.guidepedia.model.response.MessageResponse;
import com.guidepedia.model.request.SignUpRequest;
import com.guidepedia.model.entity.UserEntity;
import com.guidepedia.model.response.ProfileResponse;
import com.guidepedia.repo.UserRepository;
import com.guidepedia.security.jwt.JwtUtils;
import com.guidepedia.security.services.UserDetailsImpl;
import com.guidepedia.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@SecurityRequirement(name = "Authorization")
@Tag(description = "Api to manage users",
        name = "User Resource")
@CrossOrigin
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);


    @Operation(summary = "Get profile",
            description = "Get user's profile")
    @GetMapping("/user/profile")
    @Cacheable("profile")
    public ProfileResponse getProfile(@AuthenticationPrincipal UserDetailsImpl user) {
        return userService.getProfile(user);
    }

    @Operation(summary = "Get profile by id",
            description = "Get user's profile by UserId")
    @GetMapping("/user/profile/{userId}")
    @Cacheable("profileById")
    public ProfileResponse getProfileById(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl user) {
        return userService.getProfileById(user, userId);
    }

    @Operation(summary = "Update profile",
            description = "Provides new updated user profile")
    @PutMapping(value = "/user/profile")
    @ResponseBody
    public ProfileResponse updateProfile(@RequestBody ProfileResponse profileResponse, @AuthenticationPrincipal UserDetailsImpl user){
        return userService.updateProfile(profileResponse, user);
    }

    @Operation(summary = "Update article's save status",
            description = "Update article's save status")
    @PostMapping(value = "/user/save/article/{articleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArticleResponse changeSaveArticle(@PathVariable Long articleId, @RequestParam("status") Boolean status, @AuthenticationPrincipal UserDetailsImpl user) {
        return userService.changeSaveArticle(articleId, status, user);
    }

    @Operation(summary = "Get user's saved articles",
            description = "Get user's saved articles")
    @GetMapping("/user/saved/articles")
    @Cacheable("articleSave")
    public List<ArticleResponse> getSavedArticles(@AuthenticationPrincipal UserDetailsImpl user) {
        return userService.getSavedArticles(user);
    }

    @Operation(summary = "Update user's subscribtion status",
            description = "Update user's subscribtion status")
    @PostMapping(value = "/user/subscribtion/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileResponse changeStatusSubscribtion(@PathVariable Long userId, @RequestParam("status") Boolean status, @AuthenticationPrincipal UserDetailsImpl user) {
        return userService.changeStatusSubscribtion(userId, status, user);
    }

    @Operation(summary = "Get user's subscribers",
            description = "Get user's subscribers")
    @GetMapping("/user/subscribers")
    public List<ProfileResponse> getSubscribers(@AuthenticationPrincipal UserDetailsImpl user) {
        return userService.getSubscribers(user);
    }

    @Operation(summary = "Get user's subscribtions",
            description = "Get user's subscribtions")
    @GetMapping("/user/subscribtion")
    public List<ProfileResponse> getSubscribtions(@AuthenticationPrincipal UserDetailsImpl user) {
        return userService.getSubscribtions(user);
    }

    @Operation(summary = "Get user's subscribtions by UserId",
            description = "Get user's subscriptions by UserId")
    @GetMapping("/user/subscriptions/{userId}")
    public List<ProfileResponse> getSubscribtionsById(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable Long userId) {
        return userService.getUserSubscribtions(user, userId);
    }

    @Operation(summary = "Get user's subscribers by UserId",
            description = "Get user's subscribers by UserId")
    @GetMapping("/user/subscribers/{userId}")
    public List<ProfileResponse> getUserSubscribers(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable Long userId) {
        return userService.getUserSubscribers(user, userId);
    }


    @Operation(summary = "Get user's subscription by UserId",
            description = "Get user's subscription by UserId")
    @GetMapping("/user/subscription/{userId}")
    public List<ProfileResponse> getUserSubscription(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable Long userId) {
        return userService.getUserSubscribtions(user, userId);
    }

    /* @Operation(summary = "Get user's articles by UserId",
            description = "Get user's articles by UserId")
    @GetMapping(value = "/user/articles/{userId}")
    @ResponseBody
    @Cacheable("articlesByArticleId")
    public List<ArticleResponse> getArticlesByUserId(@PathVariable Integer userId, @AuthenticationPrincipal UserDetailsImpl user) {
        return articleService.getArticleByCategoryId(categoryId, user);
    }
     */
}
