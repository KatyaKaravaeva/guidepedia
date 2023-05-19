package com.guidepedia.repo;

import com.guidepedia.model.entity.ArticleEntity;
import com.guidepedia.model.entity.CommentEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @CachePut("comments")
    List<CommentEntity> findByArticle(ArticleEntity article);

    @Caching(
            evict = {
                    @CacheEvict("articlesCreated"),
                    @CacheEvict("articlesCategory"),
                    @CacheEvict("articles"),
                    @CacheEvict("article"),
                    @CacheEvict("comments")
            })
    CommentEntity save(CommentEntity comment);
}