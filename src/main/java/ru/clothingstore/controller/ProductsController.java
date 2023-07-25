package ru.clothingstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.clothingstore.model.product.Product;
import ru.clothingstore.model.product.ProductType;
import ru.clothingstore.service.Impl.ProductTypesService;
import ru.clothingstore.service.Impl.ProductsService;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {
    private final ProductsService productsService;
    private final ProductTypesService productTypesService;

    @Autowired
    public ProductsController(ProductsService productsService, ProductTypesService productTypesService) {
        this.productsService = productsService;
        this.productTypesService = productTypesService;
    }

    //                        @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
    //                        @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(3) int limit,
    //                        @RequestParam(value = "sort", defaultValue = "name") String sort)

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                        @RequestParam(value = "limit", defaultValue = "5", required = false) Integer limit,
                        @RequestParam(value = "sort", defaultValue = "name", required = false) String sort) {

        if (offset == 0 || limit == 0)
            model.addAttribute("products", productsService.findAll(sort));
        else
            model.addAttribute("products", productsService.findAll(offset, limit, sort));

        return "products/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("productType") ProductType productType,
                       Model model) {
        model.addAttribute("product", productsService.findOne(id));

/*       TODO*/

/*        Order productOrder = productsService.getProductOrders(id);

        if (productOrder != null)
            model.addAttribute("owner", productOwner);
        else
            model.addAttribute("people", peopleService.findAll());*/

        return "products/show";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("productTypes", productTypesService.findAll());

        return "products/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult) {
        // TODO

        /*        bookValidator.validate(book, bindingResult);*/
        if (bindingResult.hasErrors())
            return "products/new";

        productsService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("product", productsService.findOne(id));
        model.addAttribute("productTypes", productTypesService.findAll());

        return "products/edit";
    }


    @PatchMapping("/{id}")
    public String update(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "products/edit";
        }

        productsService.update(id, product);

        return "redirect:/products";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        productsService.delete(id);

        return "redirect:/products";
    }

    // TODO

/*    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        productsService.release(id);

        return "redirect:/products/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        productsService.assign(id, selectedPerson);

        return "redirect:/products/" + id;
    }*/

    @GetMapping("/search")
    public String searchPage() {
        return "products/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("products", productsService.searchByProductName(query));

        return "products/search";
    }
}
