package ru.clothingstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clothingstore.model.product.Product;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAll(Pageable pageable);

/*    List<Product> findByOwner(Person owner);*//*

    List<Product> findByName(String name);*/

    List<Product> findByNameStartingWith(String name);
}
