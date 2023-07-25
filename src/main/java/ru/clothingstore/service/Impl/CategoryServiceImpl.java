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
        LOGGER.info("Category loaded: {}.", category);
        return category;
    }

    @Override
    @Transactional
    public void addCategory(Category category) {
        categoryRepository.save(category);
        LOGGER.info("Category added: ", category);
    }

    @Override
    @Transactional
    public void updateCategory(Category category) {
        categoryRepository.save(category);
        LOGGER.info("Category updated: ", category);
    }

    @Override
    @Transactional
    public void deleteCategoryById(int id) {
        Category category = categoryRepository.getById(id);
        if(category != null){
            LOGGER.info("Category removed: {}.", category);
            categoryRepository.delete(category);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Category> getAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return new HashSet<>(categoryList);
    }
}
