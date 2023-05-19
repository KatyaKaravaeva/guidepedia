package com.guidepedia.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guidepedia.model.response.ProfileResponse;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "article", schema = "public")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="createdby")
    private UserEntity createdBy;

    @ManyToOne(optional=false)
    @JoinColumn(name="categoryid")
    private CategoryEntity category;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updatedat")
    private LocalDateTime updatedAt;

    @Column(name = "draft")
    private Boolean draft;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name="reaction",
            joinColumns=@JoinColumn (name="articleid"),
            inverseJoinColumns=@JoinColumn(name="userid"))
    private Set<UserEntity> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name="saved_article",
            joinColumns=@JoinColumn (name="articleid"),
            inverseJoinColumns=@JoinColumn(name="userid"))
    private Set<UserEntity> savedUsers = new HashSet<>();

    public void addUser(UserEntity user) {
        this.users.add(user);
        user.getArticlesReaction().add(this);
    }

    public void removeUser(UserEntity user) {
        this.users.remove(user);
        user.getArticlesReaction().remove(this);
    }

    public void addSavedUser(UserEntity user) {
        this.savedUsers.add(user);
        user.getSavedArticles().add(this);
    }

    public void removeSavedUser(UserEntity user) {
        this.savedUsers.remove(user);
        user.getSavedArticles().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleEntity)) return false;
        return id != null && id.equals(((ArticleEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}