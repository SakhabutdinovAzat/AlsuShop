package ru.alsushop.AlsuShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alsushop.AlsuShop.model.Order;
import ru.alsushop.AlsuShop.model.ProductType;
import ru.alsushop.AlsuShop.service.ProductTypesService;

import javax.validation.Valid;

@Controller
@RequestMapping("/productTypes")
public class ProductTypesController {

    private final ProductTypesService productTypesService;

    @Autowired
    public ProductTypesController(ProductTypesService productTypesService) {
        this.productTypesService = productTypesService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                        @RequestParam(value = "limit", defaultValue = "5", required = false) Integer limit,
                        @RequestParam(value = "sort", defaultValue = "name", required = false) String sort) {

        if (offset == 0 || limit == 0)
            model.addAttribute("productTypes", productTypesService.findAll(sort));
        else
            model.addAttribute("productTypes", productTypesService.findAll(offset, limit, sort));

        return "productTypes/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("order") Order order,
                       Model model) {
        model.addAttribute("productType", productTypesService.findOne(id));

        /*       TODO*/

/*        Order productOrder = productsService.getProductOrders(id);

        if (productOrder != null)
            model.addAttribute("owner", productOwner);
        else
            model.addAttribute("people", peopleService.findAll());*/

        return "productTypes/show";
    }

    @GetMapping("/new")
    public String newProductTypes(Model model) {
        model.addAttribute("productType", new ProductType());

        return "productTypes/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("productType") @Valid ProductType productType, BindingResult bindingResult) {
        // TODO

        /*        bookValidator.validate(book, bindingResult);*/
        if (bindingResult.hasErrors())
            return "productTypes/new";

        productTypesService.save(productType);
        return "redirect:/productTypes";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("productType", productTypesService.findOne(id));

        return "productTypes/edit";
    }


    @PatchMapping("/{id}")
    public String update(@ModelAttribute("productType") @Valid ProductType productType, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "productTypes/edit";
        }

        productTypesService.update(id, productType);

        return "redirect:/productTypes";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        productTypesService.delete(id);

        return "redirect:/productTypes";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "productTypes/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("productTypes", productTypesService.searchByProductTypeName(query));

        return "productTypes/search";
    }
}
