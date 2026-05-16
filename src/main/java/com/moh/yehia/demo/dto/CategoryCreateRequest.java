package com.moh.yehia.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload for creating a category.
 *
 * @param name the category name
 * @param description the category description
 */
public record CategoryCreateRequest(
        @NotBlank(message = "name must not be blank")
        @Size(max = 255, message = "name must be at most 255 characters")
        String name,
        @NotBlank(message = "description must not be blank")
        @Size(max = 255, message = "description must be at most 255 characters")
        String description) {
}

