package com.guidepedia.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "users", schema = "public")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    @Column(name = "login", unique = true)
    private String login;

    @NotNull
    @Column(name = "password_hash", unique = true)
    private String password_hash;

    @Column(name = "username")
    private String username;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "profile")
    private String profile;

    @Column(name = "card_details")
    private String card_details;

    @NotNull
    @Column(name = "status")
    private Boolean status;

    @NotNull
    @Column(name = "user_role")
    private String user_role;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
