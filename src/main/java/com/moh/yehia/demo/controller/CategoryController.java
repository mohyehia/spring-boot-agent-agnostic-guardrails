package com.moh.yehia.demo.controller;

import com.moh.yehia.demo.dto.CategoryCreateRequest;
import com.moh.yehia.demo.dto.CategoryResponse;
import com.moh.yehia.demo.dto.CategoryUpdateRequest;
import com.moh.yehia.demo.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for category CRUD operations.
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    /**
     * Creates the controller.
     *
     * @param categoryService the category service contract
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Creates a new category.
     *
     * @param request the category creation request
     * @return the created category response
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryCreateRequest request) {
        CategoryResponse categoryResponse = this.categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }

    /**
     * Returns a category by identifier.
     *
     * @param id the category identifier
     * @return the matching category response
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        log.info("action=getCategory id={}", id);
        return ResponseEntity.ok(this.categoryService.getCategoryById(id));
    }

    /**
     * Returns all categories.
     *
     * @return all category responses
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        log.info("action=listCategories");
        return ResponseEntity.ok(this.categoryService.getAllCategories());
    }

    /**
     * Updates an existing category.
     *
     * @param id the category identifier
     * @param request the category update request
     * @return the updated category response
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id, @Valid @RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(this.categoryService.updateCategory(id, request));
    }

    /**
     * Deletes an existing category.
     *
     * @param id the category identifier
     * @return an empty no-content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        this.categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

