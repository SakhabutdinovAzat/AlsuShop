package ru.clothingstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.Order;
import ru.clothingstore.model.Person;
import ru.clothingstore.repository.OrdersRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrdersService {

    private final OrdersRepository ordersRepository;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public List<Order> findAll(String sort){
        return ordersRepository.findAll(Sort.by(sort));
    }

    public List<Order> findAll(int offset, int limit, String sort){
        return ordersRepository.findAll(PageRequest.of(offset,limit, Sort.by(sort))).getContent();
    }

    public Order findOne(int id){
        Optional<Order> optionalOrder = ordersRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    @Transactional
    public void save(Order order) {
        ordersRepository.save(order);
    }

    @Transactional
    public void update(int id, Order updateOrder) {
        Order orderToBeUpdated = ordersRepository.findById(id).get();

        updateOrder.setId(id);
        updateOrder.setOwner(orderToBeUpdated.getOwner()); // чтобы не терялась связь при обновлении

        // TODO

        updateOrder.setProducts(orderToBeUpdated.getProducts());
        ordersRepository.save(updateOrder);
    }

    @Transactional
    public void delete(int id) {
        ordersRepository.deleteById(id);
    }

    public Person getOrderOwner(int id) {
        Order order = ordersRepository.findById(id).orElse(null);
        return order.getOwner();
    }

    public List<Order> findByOwner(Person person) {
        return ordersRepository.findByOwner(person);
    }

    @Transactional
    public void release(int id){
        ordersRepository.findById(id).ifPresent(
                // TODO
                item -> {
                    item.setOwner(null);
                    item.setOrderDate(null);
                });
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        ordersRepository.findById(id).ifPresent(
                // TODO
                item -> {
                    item.setOwner(selectedPerson);
                    item.setOrderDate(new Date());
                });
    }
}
