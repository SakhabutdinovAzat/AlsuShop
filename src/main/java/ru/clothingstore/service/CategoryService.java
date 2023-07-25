package ru.clothingstore.service;

import ru.clothingstore.model.good.Category;

import java.util.Set;

public interface CategoryService {

    Category getCategoryById(int id);

    void addCategory(Category category);

    void updateCategory(Category category);

    void deleteCategoryById(int id);

    Set<Category> getAllCategory();
}
