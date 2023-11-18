package ru.clothingstore.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.good.Category;
import ru.clothingstore.repository.CategoryRepository;
import ru.clothingstore.service.CategoryService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(int id) {
        Category category = categoryRepository.getById(id);
        return category;
    }

    @Override
    @Transactional
    public void addCategory(Category category) {
        categoryRepository.save(category);
        LOGGER.info("Category was added successfully: " + category);
    }

    @Override
    @Transactional
    public void updateCategory(Category category) {
        categoryRepository.save(category);
        LOGGER.info("Category was updated successfully: " + category);
    }

    @Override
    @Transactional
    public void deleteCategoryById(int id) {
        Category category = categoryRepository.getById(id);
        if(category != null){
            categoryRepository.delete(category);
            LOGGER.info("Category was removed successfully: " + category);
        } else {
            LOGGER.warn("Category was not deleted, because is null");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Category> getAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return new HashSet<>(categoryList);
    }
}
