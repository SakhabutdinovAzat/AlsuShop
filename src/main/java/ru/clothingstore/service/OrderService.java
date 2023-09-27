package ru.clothingstore.service;

import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.User;

import java.util.List;

public interface OrderService {
    public List<Order> getAllOrders(String sort);

    public List<Order> getAllOrders(int offset, int limit, String sort);

    public Order getOrderById(int id);

    public void create(User user);

    public void save(Order order);

    public void update(Order updateOrder);

    public void delete(int id);

    public User getOrderOwner(int id);

    public List<Order> getByOwner(User user);

    public void release(int id);

    public void assign(int id, User selectedUser);
}
