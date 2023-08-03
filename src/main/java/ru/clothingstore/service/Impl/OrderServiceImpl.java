package ru.clothingstore.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.Person;
import ru.clothingstore.repository.OrderRepository;
import ru.clothingstore.service.OrderService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders(String sort){
        return orderRepository.findAll(Sort.by(sort));
    }

    @Override
    public List<Order> getAllOrders(int offset, int limit, String sort){
        return orderRepository.findAll(PageRequest.of(offset,limit, Sort.by(sort))).getContent();
    }

    @Override
    public Order getOrderById(int id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    @Override
    @Transactional
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void update(Order updateOrder) {
        int orderId = updateOrder.getId();
        Order orderToBeUpdated = orderRepository.findById(orderId).get();

        updateOrder.setId(orderId);
        updateOrder.setOwner(orderToBeUpdated.getOwner()); // чтобы не терялась связь при обновлении
        updateOrder.setCart(orderToBeUpdated.getCart()); // чтобы не терялась связь при обновлении
        // TODO

        updateOrder.setProducts(orderToBeUpdated.getProducts());
        orderRepository.save(updateOrder);
    }

    @Override
    @Transactional
    public void delete(int id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Person getOrderOwner(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        return order.getOwner();
    }

    @Override
    public List<Order> findByOwner(Person person) {
        return orderRepository.findByOwner(person);
    }

    @Override
    @Transactional
    public void release(int id){
        orderRepository.findById(id).ifPresent(
                // TODO
                item -> {
                    item.setOwner(null);
                    item.setOrderDate(null);
                });
    }

    @Override
    @Transactional
    public void assign(int id, Person selectedPerson) {
        orderRepository.findById(id).ifPresent(
                // TODO
                item -> {
                    item.setOwner(selectedPerson);
                    item.setOrderDate(new Date());
                });
    }
}
