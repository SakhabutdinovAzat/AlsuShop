package ru.clothingstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clothingstore.model.cart.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

}
