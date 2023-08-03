package ru.clothingstore.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.service.OrderService;

import javax.validation.Valid;

@Controller(value = "adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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

        return "admin/order/index";
    }

    @GetMapping("/{id}/update")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("order", orderService.getOrderById(id));

        return "admin/order/update";
    }


    @PostMapping("/update")
    public String update(@ModelAttribute("order") @Valid Order order, BindingResult bindingResult,
                         RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return "admin/order/update";
        }

        orderService.update(order);
        redirectAttrs.addFlashAttribute("success", "Order has updated: " + order);
        return "redirect:admin/order";
    }
}
