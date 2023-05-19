package com.guidepedia.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guidepedia.model.entity.ArticleEntity;
import com.guidepedia.model.entity.UserEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    private Long userId;
    private String login;
    private String username;
    private String avatar;
    private String profile;
    private String cardDetails;
    private Boolean status;
    private String userRole;
    private LocalDateTime createdAt;
    private Boolean statusSubscribtion;

//    public static ProfileResponse toProfile(UserEntity user, ) {
//        ModelMapper modelMapper = new ModelMapper();
//        return modelMapper.map(user, ProfileResponse.class);
//    }

    public ProfileResponse(UserEntity user, Boolean statusSubscribtion) {
        this.userId = user.getId();
        this.login = user.getLogin();
        this.username = user.getUsername();
        this.avatar = user.getAvatar();
        this.profile = user.getProfile();
        this.cardDetails = user.getCardDetails();
        this.status = user.getStatus();
        this.userRole = user.getUserRole();
        this.createdAt = user.getCreatedAt();
        this.statusSubscribtion = statusSubscribtion;
    }

    public List<ProfileResponse> getListProfileResponces(List<UserEntity> userEntities, UserEntity user) {
        List<ProfileResponse> profileResponses = new ArrayList<>();
        for (UserEntity userEntity: userEntities) {
            profileResponses.add(new ProfileResponse(userEntity, userEntity.getSubscribers().contains(user)));
        }
        return profileResponses;
    }
}