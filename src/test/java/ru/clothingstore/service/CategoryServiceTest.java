package ru.clothingstore.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clothingstore.model.good.Category;
import ru.clothingstore.repository.CategoryRepository;
import ru.clothingstore.service.Impl.CategoryServiceImpl;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    Category category;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void before(){
        category = new Category();
    }

    @Test
    void getCategoryById() {
        when(categoryRepository.getById(1)).thenReturn(category);

        Category category1 = categoryService.getCategoryById(1);

        verify(categoryRepository, times(1)).getById(1);
        Assertions.assertEquals(category, category1);
    }

    @Test
    void addCategory() {
        categoryService.addCategory(category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void updateCategory() {
        categoryService.addCategory(category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void deleteCategoryById() {
        when(categoryRepository.getById(1)).thenReturn(category);

        categoryService.deleteCategoryById(1);

        verify(categoryRepository, times(1)).getById(1);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void getAllCategory() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        Set<Category> categoryList = categoryService.getAllCategory();

        verify(categoryRepository, times(1)).findAll();
        Assertions.assertTrue(categoryList.contains(category));
    }
}