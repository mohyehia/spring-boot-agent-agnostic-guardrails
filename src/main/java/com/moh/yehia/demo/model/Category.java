package com.moh.yehia.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents a category persisted in the database.
 */
@Table("categories")
public record Category(@Id Long id, String name, String description) {
}

