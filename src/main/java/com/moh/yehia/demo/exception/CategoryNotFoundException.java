package com.moh.yehia.demo.exception;

/**
 * Thrown when a category cannot be found for the requested identifier.
 */
public final class CategoryNotFoundException extends ApplicationException {

    /**
     * Creates a new exception for a missing category.
     *
     * @param id the missing category identifier
     */
    public CategoryNotFoundException(Long id) {
        super("Category not found for id=" + id);
    }
}

