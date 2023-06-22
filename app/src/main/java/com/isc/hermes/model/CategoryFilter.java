package com.isc.hermes.model;

public class CategoryFilter {
    private int imageCategory;
    private String nameCategory;

    public CategoryFilter(int imageCategory, String nameCategory) {
        this.imageCategory = imageCategory;
        this.nameCategory = nameCategory;
    }

    public int getImageCategory() {
        return imageCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }
}
