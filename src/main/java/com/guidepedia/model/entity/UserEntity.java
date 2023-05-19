package com.guidepedia.model.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "users", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "login")
        })
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "login", unique = true)
    private String login;

    @NotNull
    @Column(name = "passwordhash", unique = true)
    private String passwordHash;

    @Column(name = "username")
    private String username;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "profile")
    private String profile;

    @Column(name = "carddetails")
    private String cardDetails;

    @Column(name = "status")
    private Boolean status;

    @NotNull
    @Column(name = "userrole")
    private String userRole;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy="createdBy", fetch= FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ArticleEntity> article;

    @ManyToMany(mappedBy = "users", fetch= FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<ArticleEntity> articlesReaction;

    @ManyToMany(mappedBy = "savedUsers", fetch= FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<ArticleEntity> savedArticles;

    @ManyToMany(fetch= FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "subscribtion",
            joinColumns = { @JoinColumn(name = "publisherid") },
            inverseJoinColumns = { @JoinColumn(name = "subscriberid") }
    )
    private Set<UserEntity> subscribers = new HashSet<>();

    @ManyToMany(mappedBy = "subscribers", fetch= FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<UserEntity> subscriptions = new HashSet<>();

    public void addSubscriber(UserEntity user) {
        this.subscribers.add(user);
        user.getSubscriptions().add(this);
    }

    public void removeSubscriber(UserEntity user) {
        this.subscribers.remove(user);
        user.getSubscriptions().remove(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
