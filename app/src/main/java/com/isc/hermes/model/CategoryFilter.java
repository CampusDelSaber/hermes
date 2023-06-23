package com.isc.hermes.model;

/**
 * Represents a category filter for filtering items based on an image and name.
 */
public class CategoryFilter {
    private int imageCategory;
    private String nameCategory;

    /**
     * Constructs a new CategoryFilter instance with the specified image and name.
     *
     * @param imageCategory the image representing the category
     * @param nameCategory  the name of the category
     */
    public CategoryFilter(int imageCategory, String nameCategory) {
        this.imageCategory = imageCategory;
        this.nameCategory = nameCategory;
    }

    /**
     * Gets the image representing the category.
     *
     * @return the image representing the category
     */
    public int getImageCategory() {
        return imageCategory;
    }

    /**
     * Gets the name of the category.
     *
     * @return the name of the category
     */
    public String getNameCategory() {
        return nameCategory;
    }
}
