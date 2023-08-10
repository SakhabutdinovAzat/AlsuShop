package ru.clothingstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.Person;
import ru.clothingstore.service.OrderService;
import ru.clothingstore.service.PersonService;

import javax.validation.Valid;

@Controller
@RequestMapping("/orders")
public class OrderControllerOld {

    private final OrderService orderService;
    private final PersonService personService;

    @Autowired
    public OrderControllerOld(OrderService orderService, PersonService personService) {
        this.orderService = orderService;
        this.personService = personService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                        @RequestParam(value = "limit", defaultValue = "5", required = false) Integer limit,
                        @RequestParam(value = "sort", defaultValue = "orderDate", required = false) String sort) {

        if (offset == 0 || limit == 0)
            model.addAttribute("orders", orderService.getAllOrders(sort));
        else
            model.addAttribute("orders", orderService.getAllOrders(offset, limit, sort));

        return "orders/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("person") Person person,
                       Model model) {
        model.addAttribute("order", orderService.getOrderById(id));

        Person orderOwner = orderService.getOrderOwner(id);

        if (orderOwner != null)
            model.addAttribute("owner", orderOwner);
        else
            model.addAttribute("people", personService.findAll());

        return "orders/show";
    }

    @GetMapping("/new")
    public String newOrder(Model model) {
        model.addAttribute("order", new Order());
        return "orders/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("order") @Valid Order order, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "orders/new";

        orderService.save(order);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("order", orderService.getOrderById(id));

        return "orders/edit";
    }


    @PatchMapping("/{id}")
    public String update(@ModelAttribute("order") @Valid Order order, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "orders/edit";
        }

        orderService.update(order);

        return "redirect:/orders";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        orderService.delete(id);

        return "redirect:/orders";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        orderService.release(id);

        return "redirect:/orders/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        orderService.assign(id, selectedPerson);

        return "redirect:/orders/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "orders/search";
    }
}
