package ru.alsushop.AlsuShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alsushop.AlsuShop.model.Order;
import ru.alsushop.AlsuShop.model.Person;
import ru.alsushop.AlsuShop.model.Product;
import ru.alsushop.AlsuShop.service.OrdersService;
import ru.alsushop.AlsuShop.service.PeopleService;

import javax.validation.Valid;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;
    private final PeopleService peopleService;

    @Autowired
    public OrdersController(OrdersService ordersService, PeopleService peopleService) {
        this.ordersService = ordersService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                        @RequestParam(value = "limit", defaultValue = "5", required = false) Integer limit,
                        @RequestParam(value = "sort", defaultValue = "orderDate", required = false) String sort) {

        if (offset == 0 || limit == 0)
            model.addAttribute("orders", ordersService.findAll(sort));
        else
            model.addAttribute("orders", ordersService.findAll(offset, limit, sort));

        return "orders/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("person") Person person,
                       Model model) {
        model.addAttribute("order", ordersService.findOne(id));

        Person orderOwner = ordersService.getOrderOwner(id);

        if (orderOwner != null)
            model.addAttribute("owner", orderOwner);
        else
            model.addAttribute("people", peopleService.findAll());

        return "orders/show";
    }

    @GetMapping("/new")
    public String newItem(Model model) {
        model.addAttribute("order", new Order());

        return "orders/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("order") @Valid Order order, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "orders/new";

        ordersService.save(order);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("order", ordersService.findOne(id));

        return "orders/edit";
    }


    @PatchMapping("/{id}")
    public String update(@ModelAttribute("order") @Valid Order order, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "orders/edit";
        }

        ordersService.update(id, order);

        return "redirect:/orders";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        ordersService.delete(id);

        return "redirect:/orders";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        ordersService.release(id);

        return "redirect:/orders/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        ordersService.assign(id, selectedPerson);

        return "redirect:/orders/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "orders/search";
    }
}
