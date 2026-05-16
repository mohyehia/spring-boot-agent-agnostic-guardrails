package com.moh.yehia.demo.service;

import com.moh.yehia.demo.dto.CategoryCreateRequest;
import com.moh.yehia.demo.dto.CategoryResponse;
import com.moh.yehia.demo.dto.CategoryUpdateRequest;
import com.moh.yehia.demo.exception.CategoryNotFoundException;
import com.moh.yehia.demo.model.Category;
import com.moh.yehia.demo.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for category CRUD operations.
 */
@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Creates the service.
     *
     * @param categoryRepository the category repository
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates a new category.
     *
     * @param request the category creation payload
     * @return the created category response
     */
    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        Category createdCategory = this.categoryRepository.save(
                new Category(null, request.name(), request.description()));
        return this.toResponse(createdCategory);
    }

    /**
     * Returns a category by identifier.
     *
     * @param id the category identifier
     * @return the matching category response
     */
    public CategoryResponse getCategoryById(Long id) {
        return this.categoryRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    /**
     * Returns all categories.
     *
     * @return all category responses
     */
    public List<CategoryResponse> getAllCategories() {
        return this.categoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Updates an existing category.
     *
     * @param id the category identifier
     * @param request the category update payload
     * @return the updated category response
     */
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        Category existingCategory = this.categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        Category updatedCategory = new Category(
                existingCategory.id(),
                request.name(),
                request.description());
        return this.toResponse(this.categoryRepository.save(updatedCategory));
    }

    /**
     * Deletes an existing category.
     *
     * @param id the category identifier
     */
    @Transactional
    public void deleteCategory(Long id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        this.categoryRepository.delete(category);
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.id(), category.name(),
                category.description());
    }
}

