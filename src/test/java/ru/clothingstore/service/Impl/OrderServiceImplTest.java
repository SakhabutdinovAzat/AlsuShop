package ru.clothingstore.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.good.Good;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.User;
import ru.clothingstore.model.product.Product;
import ru.clothingstore.repository.OrderRepository;
import ru.clothingstore.repository.UserRepository;
import ru.clothingstore.service.CartService;
import ru.clothingstore.service.MailService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private Order order;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartService cartService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MailService mailService;

    @BeforeEach
    void beforeEach() {
        order = new Order();
        order.setId(1);

    }
    @Test
    void getAllOrders() {
        Order order1 = new Order();
        when(orderRepository.findAll(Sort.by("orderDate"))).thenReturn(List.of(order, order1));

        orderService.getAllOrders("orderDate");

        verify(orderRepository, times(1)).findAll(Sort.by("orderDate"));
    }

    @Test
    void getOrderById() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        Order order1 = orderService.getOrderById(1);

        verify(orderRepository, times(1)).findById(1);
        assertEquals(order, order1);
    }

    @Test
    void save() {
        orderService.save(order);

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void create() {
        User user = new User();
        Cart cart = new Cart();
        Product product = new Product("product", 1000);
        product.setGood(new Good());
        cart.setProducts(new HashSet<Product>(List.of(product)));
        user.setCart(cart);
        orderService.create(user);

        verify(orderRepository, times(1)).save(user.getOrders().get(0));
        verify(userRepository, times(1)).save(user);
        verify(cartService, times(1)).updateCart(cart);
        verify(mailService, times(1)).sendEmail(Mockito.any(), Mockito.any(), Mockito.any());

    }

    @Test
    void update() {
        Order order1 = new Order();
        order1.setId(1);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        orderService.update(order1);

        verify(orderRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).save(order1);
    }

    @Test
    void delete() {
        orderService.delete(1);
        verify(orderRepository, times(1)).deleteById(1);
    }

    @Test
    void getOrderOwner() {
        User user = new User();
        user.setUsername("user");
        order.setOwner(user);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        User user1 = orderService.getOrderOwner(1);

        verify(orderRepository, times(1)).findById(1);
        assertEquals(user, user1);
    }

    @Test
    void findByOwner() {
        User user = new User();
        user.setUsername("user");
        order.setOwner(user);
        when(orderRepository.findByOwner(user)).thenReturn(List.of(order));

        List<Order> orderList = orderService.getByOwner(user);

        verify(orderRepository, times(1)).findByOwner(user);
        assertTrue(orderList.contains(order));
    }

    @Test
    void sendEmailOrder() {
        User user = new User();
        Cart cart = new Cart();
        Product product = new Product("product", 1000);
        product.setGood(new Good());
        cart.setProducts(new HashSet<Product>(List.of(product)));
        user.setCart(cart);
        order.setCart(cart);
        order.setOwner(user);
        user.addOrder(order);
        orderService.create(user);

        verify(mailService, times(1)).sendEmail(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Disabled
    @Test
    void release() {
    }

    @Disabled
    @Test
    void assign() {
    }
}