package ru.clothingstore.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.Person;
import ru.clothingstore.repository.OrderRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll(String sort){
        return orderRepository.findAll(Sort.by(sort));
    }

    public List<Order> findAll(int offset, int limit, String sort){
        return orderRepository.findAll(PageRequest.of(offset,limit, Sort.by(sort))).getContent();
    }

    public Order findOne(int id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    @Transactional
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    public void update(int id, Order updateOrder) {
        Order orderToBeUpdated = orderRepository.findById(id).get();

        updateOrder.setId(id);
        updateOrder.setOwner(orderToBeUpdated.getOwner()); // чтобы не терялась связь при обновлении

        // TODO

        updateOrder.setProducts(orderToBeUpdated.getProducts());
        orderRepository.save(updateOrder);
    }

    @Transactional
    public void delete(int id) {
        orderRepository.deleteById(id);
    }

    public Person getOrderOwner(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        return order.getOwner();
    }

    public List<Order> findByOwner(Person person) {
        return orderRepository.findByOwner(person);
    }

    @Transactional
    public void release(int id){
        orderRepository.findById(id).ifPresent(
                // TODO
                item -> {
                    item.setOwner(null);
                    item.setOrderDate(null);
                });
    }

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
