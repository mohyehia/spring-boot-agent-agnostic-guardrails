package com.moh.yehia.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.moh.yehia.demo.dto.CategoryCreateRequest;
import com.moh.yehia.demo.dto.CategoryUpdateRequest;
import com.moh.yehia.demo.exception.CategoryNotFoundException;
import com.moh.yehia.demo.model.Category;
import com.moh.yehia.demo.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoryServiceTest {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    CategoryServiceTest(
            @Autowired CategoryService categoryService,
            @Autowired CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @BeforeEach
    void setUp() {
        this.categoryRepository.deleteAll();
    }

    @Test
    void givenCategoryCreateRequest_whenCreateCategory_thenCreatedCategoryReturned() {
        // Given
        CategoryCreateRequest request = new CategoryCreateRequest("Electronics", "Electronic items");

        // When
        var response = this.categoryService.createCategory(request);

        // Then
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Electronics");
        assertThat(response.description()).isEqualTo("Electronic items");
    }

    @Test
    void givenPersistedCategory_whenGetCategoryById_thenMatchingCategoryReturned() {
        // Given
        Category savedCategory = this.categoryRepository.save(
                new Category(null, "Furniture", "Home furniture"));

        // When
        var response = this.categoryService.getCategoryById(savedCategory.id());

        // Then
        assertThat(response.id()).isEqualTo(savedCategory.id());
        assertThat(response.name()).isEqualTo("Furniture");
    }

    @Test
    void givenNoCategories_whenGetAllCategories_thenEmptyListReturned() {
        // Given

        // When
        var responses = this.categoryService.getAllCategories();

        // Then
        assertThat(responses).isEmpty();
    }

    @Test
    void givenPersistedCategories_whenGetAllCategories_thenAllCategoriesReturned() {
        // Given
        this.categoryRepository.save(new Category(null, "Books", "Books category"));
        this.categoryRepository.save(new Category(null, "Music", "Music category"));

        // When
        var responses = this.categoryService.getAllCategories();

        // Then
        assertThat(responses)
                .extracting(com.moh.yehia.demo.dto.CategoryResponse::name)
                .containsExactlyInAnyOrder("Books", "Music");
    }

    @Test
    void givenPersistedCategory_whenUpdateCategory_thenUpdatedCategoryReturned() {
        // Given
        Category savedCategory = this.categoryRepository.save(
                new Category(null, "Sports", "Sports goods"));
        CategoryUpdateRequest request = new CategoryUpdateRequest(
                "Sports", "Sports and outdoor goods");

        // When
        var response = this.categoryService.updateCategory(savedCategory.id(), request);

        // Then
        assertThat(response.id()).isEqualTo(savedCategory.id());
        assertThat(response.description()).isEqualTo("Sports and outdoor goods");
    }

    @Test
    void givenPersistedCategory_whenDeleteCategory_thenCategoryIsRemoved() {
        // Given
        Category savedCategory = this.categoryRepository.save(
                new Category(null, "Health", "Health products"));

        // When
        this.categoryService.deleteCategory(savedCategory.id());

        // Then
        assertThat(this.categoryRepository.findById(savedCategory.id())).isEmpty();
    }

    @Test
    void givenUnknownId_whenGetCategoryById_thenCategoryNotFoundExceptionThrown() {
        // Given
        long unknownId = 77L;

        // When / Then
        assertThatThrownBy(() -> this.categoryService.getCategoryById(unknownId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("Category not found for id=77");
    }

    @Test
    void givenUnknownId_whenUpdateCategory_thenCategoryNotFoundExceptionThrown() {
        // Given
        long unknownId = 88L;
        CategoryUpdateRequest request = new CategoryUpdateRequest("Garden", "Garden tools");

        // When / Then
        assertThatThrownBy(() -> this.categoryService.updateCategory(unknownId, request))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("Category not found for id=88");
    }

    @Test
    void givenUnknownId_whenDeleteCategory_thenCategoryNotFoundExceptionThrown() {
        // Given
        long unknownId = 99L;

        // When / Then
        assertThatThrownBy(() -> this.categoryService.deleteCategory(unknownId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("Category not found for id=99");
    }
}



