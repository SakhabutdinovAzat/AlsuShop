package ru.clothingstore.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.order.Status;
import ru.clothingstore.model.user.User;
import ru.clothingstore.model.product.Product;
import ru.clothingstore.repository.OrderRepository;
import ru.clothingstore.repository.UserRepository;
import ru.clothingstore.service.CartService;
import ru.clothingstore.service.MailService;
import ru.clothingstore.service.OrderService;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final MailService mailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderServiceImpl(UserRepository userRepository, CartService cartService, OrderRepository orderRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.mailService = mailService;
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

//    @Override
//    @Transactional
//    public void save(Order order) {
//        orderRepository.save(order);
//        LOGGER.info("Order was saved successfully: " + order);
//    }

    @Override
    @Transactional
    public void create(User user){
        Cart oldCart = user.getCart();
        user.setCart(new Cart());

        Order order = new Order();
        order.setCart(oldCart);
        order.setOwner(user);
        order.setOrderDate(new Date());

        order.setStatus(Status.Оформлен);
        user.addOrder(order);

        cartService.updateCart(oldCart);
        orderRepository.save(order);
        userRepository.save(user);

        if (!user.getOrders().isEmpty()) {
            sendEmailOrder(order);
        }

        List<Order> orders = new ArrayList<>(user.getOrders());
        orders.sort(Comparator.comparing(Order::getId).reversed());
        LOGGER.info("Order was created successfully: " + order);
    }

    @Override
    @Transactional
    public void update(Order updateOrder) {
        int orderId = updateOrder.getId();
        Order orderToBeUpdated = orderRepository.findById(orderId).get();

        updateOrder.setId(orderId);
        updateOrder.setOwner(orderToBeUpdated.getOwner()); // чтобы не терялась связь при обновлении
        updateOrder.setCart(orderToBeUpdated.getCart()); // чтобы не терялась связь при обновлении

        orderRepository.save(updateOrder);
        LOGGER.info("Order was updated successfully: " + updateOrder);
    }

    @Override
    @Transactional
    public void delete(int id) {
        orderRepository.deleteById(id);
        LOGGER.info("Order with id = {} was deleted successfully", id);
    }

    @Override
    public User getOrderOwner(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        return order.getOwner();
    }

    @Override
    public List<Order> getByOwner(User user) {
        return orderRepository.findByOwner(user);
    }

    public void sendEmailOrder(Order order) {
        StringBuilder products = new StringBuilder();
        for (Product product : order.getCart().getProducts())
            products.append("Good: " + product.getGood().getDescription()
                    + ". Count: " + product.getCount() + ". \n");

        String message = "Dear " + order.getOwner().getUsername()
                + ", thank you for placing order. "
                + "Your order ID is " + order.getId() + ".\n"
                + "Order status: " + order.getStatus() + "." + "\n"
                + "SUM: " + order.getCart().getSum() + " RUB.\n"
                + "Your cart: " + products;
        String subject = "Your order on ClothingStore #" + order.getId();
        mailService.sendEmail(order.getOwner().getEmail(), subject, message);
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
    public void assign(int id, User selectedUser) {
        orderRepository.findById(id).ifPresent(
                // TODO
                item -> {
                    item.setOwner(selectedUser);
                    item.setOrderDate(new Date());
                });
    }
}
