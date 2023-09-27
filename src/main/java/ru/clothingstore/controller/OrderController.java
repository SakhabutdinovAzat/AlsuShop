package ru.clothingstore.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.User;
import ru.clothingstore.service.OrderService;
import ru.clothingstore.service.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;

    public OrderController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping()
    public String getOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user  = userService.getByUsername(username).get();

        List<Order> orders = new ArrayList<>(user.getOrders());
        orders.sort(Comparator.comparing(Order::getId).reversed());
        model.addAttribute("orders", orders);

        return "order/index";
    }

    @GetMapping("/create")
    public String createOrder(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getByUsername(username).get();

        if (user.getCart().getProducts().isEmpty()) {
            return "order/index";
        }
        orderService.create(user);
        model.addAttribute("orders", orderService.getByOwner(user));
        return "order/index";
    }
}
