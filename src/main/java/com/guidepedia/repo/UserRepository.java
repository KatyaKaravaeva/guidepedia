package com.guidepedia.repo;

import com.guidepedia.model.entity.UserEntity;
import com.guidepedia.security.services.UserDetailsImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @CachePut("user")
    Optional<UserEntity> findByLogin(String login);

    @CachePut("user")
    Optional<UserEntity> findById(Long id);

    @Caching(
            evict = {
                    @CacheEvict("user")
            })
    UserEntity save(UserEntity user);

    Boolean existsByLogin(String login);
}
