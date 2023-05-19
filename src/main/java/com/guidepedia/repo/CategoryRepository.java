package com.guidepedia.repo;

import com.guidepedia.model.entity.CategoryEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    @Cacheable("category")
    Optional<CategoryEntity> findByName(String categoryName);
}