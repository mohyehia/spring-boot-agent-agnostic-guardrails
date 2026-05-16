package com.moh.yehia.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.moh.yehia.demo.dto.CategoryCreateRequest;
import com.moh.yehia.demo.dto.CategoryResponse;
import com.moh.yehia.demo.dto.CategoryUpdateRequest;
import com.moh.yehia.demo.exception.CategoryNotFoundException;
import com.moh.yehia.demo.exception.GlobalExceptionHandler;
import com.moh.yehia.demo.service.CategoryService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
@Import(GlobalExceptionHandler.class)
class CategoryControllerTest {

    private final MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    CategoryControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void givenValidRequest_whenCreateCategory_thenCreatedStatusReturned() throws Exception {
        // Given
        String request = """
                {
                  \"name\": \"Books\",
                  \"description\": \"Books section\"
                }
                """;
        given(this.categoryService.createCategory(any(CategoryCreateRequest.class)))
                .willReturn(new CategoryResponse(1L, "Books", "Books section"));

        // When / Then
        this.mockMvc.perform(post("/categories")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Books"))
                .andExpect(jsonPath("$.description").value("Books section"));
    }

    @Test
    void givenInvalidRequest_whenCreateCategory_thenBadRequestReturned() throws Exception {
        // Given
        String request = """
                {
                  \"name\": \"\",
                  \"description\": \"\"
                }
                """;

        // When / Then
        this.mockMvc.perform(post("/categories")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void givenInvalidJson_whenCreateCategory_thenBadRequestReturned() throws Exception {
        // Given
        String invalidJson = "{\"name\":\"Books\",\"description\":";

        // When / Then
        this.mockMvc.perform(post("/categories")
                        .contentType(APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body is invalid"));
    }

    @Test
    void givenExistingCategoryId_whenGetCategoryById_thenOkStatusReturned() throws Exception {
        // Given
        given(this.categoryService.getCategoryById(1L))
                .willReturn(new CategoryResponse(1L, "Books", "Books section"));

        // When / Then
        this.mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @Test
    void givenUnknownCategoryId_whenGetCategoryById_thenNotFoundReturned() throws Exception {
        // Given
        given(this.categoryService.getCategoryById(1L))
                .willThrow(new CategoryNotFoundException(1L));

        // When / Then
        this.mockMvc.perform(get("/categories/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void givenStoredCategories_whenGetAllCategories_thenOkStatusReturned() throws Exception {
        // Given
        given(this.categoryService.getAllCategories()).willReturn(List.of(
                new CategoryResponse(1L, "Books", "Books section"),
                new CategoryResponse(2L, "Music", "Music section")));

        // When / Then
        this.mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].name").value("Music"));
    }

    @Test
    void givenNoStoredCategories_whenGetAllCategories_thenEmptyArrayReturned() throws Exception {
        // Given
        given(this.categoryService.getAllCategories()).willReturn(List.of());

        // When / Then
        this.mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenValidRequest_whenUpdateCategory_thenOkStatusReturned() throws Exception {
        // Given
        String request = """
                {
                  \"name\": \"Books\",
                  \"description\": \"Updated books section\"
                }
                """;
        given(this.categoryService.updateCategory(eq(1L), any(CategoryUpdateRequest.class)))
                .willReturn(new CategoryResponse(1L, "Books", "Updated books section"));

        // When / Then
        this.mockMvc.perform(put("/categories/1")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated books section"));
    }

    @Test
    void givenInvalidRequest_whenUpdateCategory_thenBadRequestReturned() throws Exception {
        // Given
        String request = """
                {
                  \"name\": \"\",
                  \"description\": \"\"
                }
                """;

        // When / Then
        this.mockMvc.perform(put("/categories/1")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void givenUnknownCategoryId_whenUpdateCategory_thenNotFoundReturned() throws Exception {
        // Given
        String request = """
                {
                  \"name\": \"Books\",
                  \"description\": \"Updated books section\"
                }
                """;
        given(this.categoryService.updateCategory(eq(1L), any(CategoryUpdateRequest.class)))
                .willThrow(new CategoryNotFoundException(1L));

        // When / Then
        this.mockMvc.perform(put("/categories/1")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void givenExistingCategoryId_whenDeleteCategory_thenNoContentReturned() throws Exception {
        // Given
        willDoNothing().given(this.categoryService).deleteCategory(1L);

        // When / Then
        this.mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenUnknownCategoryId_whenDeleteCategory_thenNotFoundReturned() throws Exception {
        // Given
        willThrow(new CategoryNotFoundException(1L))
                .given(this.categoryService)
                .deleteCategory(1L);

        // When / Then
        this.mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}



