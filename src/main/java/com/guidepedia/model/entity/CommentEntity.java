package com.guidepedia.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "comment", schema = "public")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, cascade= CascadeType.MERGE, fetch= FetchType.EAGER)
    @JoinColumn(name="articleid")
    private ArticleEntity article;

    @ManyToOne(optional=false, cascade=CascadeType.MERGE, fetch= FetchType.EAGER)
    @JoinColumn(name="userid")
    private UserEntity user;

    @Column(name = "comment")
    private String comment;
}