package com.moh.yehia.demo.repository;

import com.moh.yehia.demo.model.Category;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for category persistence operations.
 */
@Repository
public interface CategoryRepository extends ListCrudRepository<Category, Long> {
}

