package ru.clothingstore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clothingstore.model.good.Category;
import ru.clothingstore.model.good.Good;
import ru.clothingstore.repository.CategoryRepository;
import ru.clothingstore.repository.GoodRepository;
import ru.clothingstore.service.Impl.GoodServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class GoodServiceTest {

    private Good good;
    @InjectMocks
    private GoodServiceImpl goodService;

    @Mock
    private GoodRepository goodRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void beforeEach(){
        good = new Good();
        good.setId(1);
        good.setTitle("Title");
    }

    @Test
    void getGoodByTitle() {
        when(goodRepository.findByTitle("Title")).thenReturn(Optional.of(good));
        Good good1 = goodService.getGoodByTitle("Title");

        verify(goodRepository, times(1)).findByTitle("Title");
        assertEquals(good, good1);
    }

    @Test
    void getGoodById() {
        when(goodRepository.findById(1)).thenReturn(Optional.of(good));
        Good good1 = goodService.getGoodById(1);

        verify(goodRepository, times(1)).findById(1);
        assertEquals(good, good1);
    }

    @Test
    void getAllGoods() {
        Good good1 = new Good();
        Mockito.when(goodRepository.findAll()).thenReturn(List.of(good, good1));
        Set<Good> goodSet = goodService.getAllGoods();

        verify(goodRepository, Mockito.times(1)).findAll();
        assertTrue(goodSet.containsAll(List.of(good, good1)));
    }

    @Disabled
    @Test
    void getLastAddedGoods() {
    }

    @Disabled
    @Test
    void getGoodsByCategory() {
        Good good1 = new Good();
        Category category = new Category();
        category.setId(1);

        when(categoryRepository.getById(1)).thenReturn(category);
        when(goodRepository.findGoodsByCategory(category)).thenReturn(List.of(good, good1));

        Set<Good> goodSet = goodService.getGoodsByCategory(1);

        verify(categoryRepository, times(1)).getById(1);
        verify(goodRepository, Mockito.times(1)).findGoodsByCategory(category);
        assertTrue(goodSet.containsAll(List.of(good, good1)));

    }

    @Test
    void searchByTitle() {
        Good good1 = new Good();
        when(goodRepository.findByTitleStartingWith("Tit")).thenReturn(List.of(good, good1));
        List<Good> goodList = goodService.searchByTitle("Tit");

        verify(goodRepository, Mockito.times(1)).findByTitleStartingWith("Tit");
        assertTrue(goodList.containsAll(List.of(good, good1)));
    }

    @Test
    void addGood() {
        goodService.addGood(good);
        verify(goodRepository, times(1)).save(good);
    }

    @Test
    void updateGood() {
        goodService.addGood(good);
        verify(goodRepository, times(1)).save(good);
    }

    @Test
    void deleteGood() {
        goodService.deleteGood(1);
        verify(goodRepository, times(1)).deleteById(1);
    }
}