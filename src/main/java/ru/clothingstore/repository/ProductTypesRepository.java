package ru.clothingstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clothingstore.model.ProductType;

import java.util.List;

@Repository
public interface ProductTypesRepository extends JpaRepository<ProductType, Integer> {
    Page<ProductType> findAll(Pageable pageable);

    List<ProductType> findByNameStartingWith(String name);
}
