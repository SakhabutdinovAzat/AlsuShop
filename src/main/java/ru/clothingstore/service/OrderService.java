package ru.clothingstore.service;

import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.Person;

import java.util.List;

public interface OrderService {
    public List<Order> getAllOrders(String sort);

    public List<Order> getAllOrders(int offset, int limit, String sort);

    public Order getOrderById(int id);

    public void save(Order order);

    public void update(Order updateOrder);

    public void delete(int id);

    public Person getOrderOwner(int id);

    public List<Order> findByOwner(Person person);

    public void release(int id);

    public void assign(int id, Person selectedPerson);
}
