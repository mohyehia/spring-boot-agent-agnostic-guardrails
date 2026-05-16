package com.moh.yehia.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.moh.yehia.demo.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration;

@DataJdbcTest(properties = "spring.flyway.enabled=true")
@ImportAutoConfiguration(FlywayAutoConfiguration.class)
class CategoryRepositoryTest {

    private final CategoryRepository categoryRepository;

    CategoryRepositoryTest(@Autowired CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @BeforeEach
    void setUp() {
        this.categoryRepository.deleteAll();
    }

    @Test
    void givenCategory_whenSave_thenCategoryIsPersisted() {
        // Given
        Category category = new Category(null, "Hardware", "Hardware accessories");

        // When
        Category savedCategory = this.categoryRepository.save(category);

        // Then
        assertThat(savedCategory.id()).isNotNull();
        assertThat(savedCategory.name()).isEqualTo("Hardware");
        assertThat(savedCategory.description()).isEqualTo("Hardware accessories");
    }

    @Test
    void givenUnknownId_whenFindById_thenOptionalEmptyReturned() {
        // Given
        long unknownId = 999L;

        // When
        var category = this.categoryRepository.findById(unknownId);

        // Then
        assertThat(category).isEmpty();
    }

    @Test
    void givenStoredCategories_whenFindAll_thenAllCategoriesReturned() {
        // Given
        this.categoryRepository.save(new Category(null, "Books", "Printed books"));
        this.categoryRepository.save(new Category(null, "Games", "Video games"));

        // When
        var categories = this.categoryRepository.findAll();

        // Then
        assertThat(categories)
                .extracting(Category::name)
                .containsExactlyInAnyOrder("Books", "Games");
    }

    @Test
    void givenPersistedCategory_whenUpdate_thenNewValuesAreStored() {
        // Given
        Category savedCategory = this.categoryRepository.save(
                new Category(null, "Garden", "Garden items"));
        Category updatedCategory = new Category(
                savedCategory.id(), "Garden", "Outdoor garden items");

        // When
        Category persistedCategory = this.categoryRepository.save(updatedCategory);

        // Then
        assertThat(persistedCategory.id()).isEqualTo(savedCategory.id());
        assertThat(persistedCategory.description()).isEqualTo("Outdoor garden items");
    }

    @Test
    void givenPersistedCategory_whenDelete_thenCategoryIsRemoved() {
        // Given
        Category savedCategory = this.categoryRepository.save(
                new Category(null, "Office", "Office supplies"));

        // When
        this.categoryRepository.delete(savedCategory);

        // Then
        assertThat(this.categoryRepository.findById(savedCategory.id())).isEmpty();
    }
}


