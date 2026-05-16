package com.moh.yehia.demo.dto;

/**
 * Response payload for category data.
 *
 * @param id the category identifier
 * @param name the category name
 * @param description the category description
 */
public record CategoryResponse(Long id, String name, String description) {
}

