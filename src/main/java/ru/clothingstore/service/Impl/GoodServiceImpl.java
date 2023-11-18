package ru.clothingstore.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.good.Category;
import ru.clothingstore.model.good.Good;
import ru.clothingstore.repository.CategoryRepository;
import ru.clothingstore.repository.GoodRepository;
import ru.clothingstore.service.GoodService;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class GoodServiceImpl implements GoodService {

    private final GoodRepository goodRepository;
    private final CategoryRepository categoryRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodService.class);

    @Autowired
    public GoodServiceImpl(GoodRepository goodRepository, CategoryRepository categoryRepository) {
        this.goodRepository = goodRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Good getGoodByTitle(String title) {
        return goodRepository.findByTitle(title);
    }

    @Override
    public Good getGoodByTitle(String title, boolean active) {
        return goodRepository.findByTitleAndActive(title, active);
    }

    @Override
    public Good getGoodById(int id) {
        return goodRepository.findById(id).orElse(null);
    }

    @Override
    public Good getGoodById(int id, boolean active) {
        return goodRepository.findByIdAndActive(id, active);
    }

    @Override
    public Set<Good> getAllGoods() {
        return new HashSet<>(goodRepository.findAll());
    }

    @Override
    public Set<Good> getAllGoods(boolean active) {
        return new HashSet<>(goodRepository.findAllByActive(active));
    }


    // TODO Оптимизировать
    @Override
    public Set<Good> getGoodsByCategory(int categoryId) {
        Category category = categoryRepository.getById(categoryId);
        return new HashSet<>(goodRepository.findGoodsByCategory(category));
    }

    @Override
    public Page<Good> getGoodsByCategory(int categoryId, boolean active, int offset, int limit, String sort) {
        Category category = categoryRepository.getById(categoryId);
        return goodRepository.findGoodsByCategoryAndActive(category, active, PageRequest.of(offset, limit, Sort.by(sort)));
    }

    @Override
    @Transactional
    public void addGood(Good good) {
        goodRepository.save(good);
        LOGGER.info("Good was added successfully: " + good);
    }

    @Override
    @Transactional
    public void updateGood(Good good) {
        goodRepository.save(good);
        LOGGER.info("Good was updated successfully: " + good);
    }

    @Override
    @Transactional
    public void deleteGood(int id) {
        goodRepository.deleteById(id);
        LOGGER.info("Good with id = {} was deleted successfully", id);
    }



    // TODO
    @Override
    public Set<Good> getGoods(int count) {
        return null;
    }

    @Override
    public Set<Good> getGoods(int count, boolean active) {
        Set<Good> goods = new LinkedHashSet<>(goodRepository.findByActiveOrderByIdDesc(active).subList(0,count));
        return goods;
    }

    // TODO
    @Override
    public Set<Good> getLastAddedGoods(int count) {
        return null;
    }

    @Override
    public Set<Good> getLastAddedGoods(int count, boolean active) {
        return new HashSet<>(goodRepository.findTop4ByActive(active));
    }

    @Override
    public List<Good> searchByTitle(String title) {
        return goodRepository.findByTitleStartingWith(title);
    }

    // TODO
    @Override
    public Set<Good> getGoodsByCategory(int count, int categoryId) {
        return null;
    }

    @Override
    public Set<Good> getGoodsByCategory(int count, int categoryId, boolean active) {
        return null;
    }
}
