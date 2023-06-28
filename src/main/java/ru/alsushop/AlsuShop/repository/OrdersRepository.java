package ru.alsushop.AlsuShop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alsushop.AlsuShop.model.Order;
import ru.alsushop.AlsuShop.model.Person;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAll(Pageable pageable);

    List<Order> findByOwner(Person owner);
}
