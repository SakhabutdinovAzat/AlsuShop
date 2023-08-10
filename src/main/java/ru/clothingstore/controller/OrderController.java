package ru.clothingstore.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.order.Status;
import ru.clothingstore.model.person.Person;
import ru.clothingstore.service.CartService;
import ru.clothingstore.service.OrderService;
import ru.clothingstore.service.PersonService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final PersonService personService;
    private final OrderService orderService;
    private final CartService cartService;
    //TODO mail service реализовать

    public OrderController(PersonService personService, OrderService orderService, CartService cartService) {
        this.personService = personService;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping()
    public String getOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Person person = personService.findByUsername(username).get();

        List<Order> orders = new ArrayList<>(person.getOrders());
        orders.sort(Comparator.comparing(Order::getId).reversed());
        model.addAttribute("orders", orders);
        return "order/index";
    }

    @GetMapping("/create")
    public String createOrder(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Person person = personService.findByUsername(username).get();

        if (person.getCart().getProducts().isEmpty()) {
            return "order/index";
        }

        Cart oldCart = person.getCart();
        person.setCart(new Cart());

        Order order = new Order();
        order.setCart(oldCart);
        order.setOwner(person);

        order.setStatus(Status.Оформлен);
        person.addOrder(order);

        cartService.updateCart(oldCart);
        orderService.save(order);
        personService.update(person);


        // TODO реализовать send email for user
//        mailService.sendEmail(order);

        List<Order> orders = new ArrayList<>(person.getOrders());
        orders.sort(Comparator.comparing(Order::getId).reversed());
        model.addAttribute("orders", orders);
        return "order/index";
    }
}
