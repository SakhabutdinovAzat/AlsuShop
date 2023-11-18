package ru.clothingstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clothingstore.model.good.Category;
import ru.clothingstore.model.good.Good;

import java.util.List;

@Repository
public interface GoodRepository extends JpaRepository<Good, Integer> {
    Good findByTitle(String title);

    Good findByTitleAndActive(String title, boolean active);

    Good findByIdAndActive(int id, boolean active);

    List<Good> findGoodsByCategory(Category category);

    Page<Good> findGoodsByCategoryAndActive(Category category, boolean active, Pageable pageable);

    Page<Good> findAllByActive(boolean active, Pageable pageable);

    List<Good> findTop4ByActive(boolean active);

    List<Good> findByTitleStartingWith(String title);

    List<Good> findByActiveOrderByIdDesc(boolean active);
}
