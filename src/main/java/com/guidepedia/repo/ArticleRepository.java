package com.guidepedia.repo;

import com.guidepedia.model.entity.ArticleEntity;
import com.guidepedia.model.entity.CategoryEntity;
import com.guidepedia.model.entity.UserEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    @CachePut("articlesCreated")
    List<ArticleEntity> findAllByCreatedByOrderByCreatedAtDesc(UserEntity createdBy);
    List<ArticleEntity> findAllByCreatedByAndDraftOrderByCreatedAtDesc(UserEntity createdBy, Boolean draft);
    @CachePut("articlesCategory")
    List<ArticleEntity> findAllByCategoryIdOrderByCreatedAtDesc(Integer categoryId);
    @CachePut("articles")
    List<ArticleEntity> findAll();

    @Caching(
            evict = {
                    @CacheEvict("articlesCreated"),
                    @CacheEvict("articlesCategory"),
                    @CacheEvict("articles"),
                    @CacheEvict("article"),
                    @CacheEvict("articlesearch")
            })
    ArticleEntity save(ArticleEntity article);

    @CachePut("article")
    Optional<ArticleEntity> findById(Long id);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM ArticleEntity a JOIN a.users u WHERE u.login = :login")
    boolean existsUserByLogin(String login);

    @CachePut("articlesearch")
    @Query("select e from ArticleEntity e where lower(e.title) like %:title%")
    List<ArticleEntity> findByTitleContainingIgnoreCase(String title);
}
