//package ru.clothingstore.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import ru.clothingstore.model.product.Product;
//import ru.clothingstore.service.Impl.ProductsService;
//
//import javax.validation.Valid;
//
//@Controller
//@RequestMapping("/products")
//public class ProductsControllerOld {
//    private final ProductsService productsService;
//
//    @Autowired
//    public ProductsControllerOld(ProductsService productsService) {
//        this.productsService = productsService;
//    }
//
//    //                        @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
//    //                        @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(3) int limit,
//    //                        @RequestParam(value = "sort", defaultValue = "name") String sort)
//
//    @GetMapping()
//    public String index(Model model,
//                        @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
//                        @RequestParam(value = "limit", defaultValue = "5", required = false) Integer limit,
//                        @RequestParam(value = "sort", defaultValue = "name", required = false) String sort) {
//
//        if (offset == 0 || limit == 0)
//            model.addAttribute("products", productsService.findAll(sort));
//        else
//            model.addAttribute("products", productsService.findAll(offset, limit, sort));
//
//        return "products/index";
//    }
//
//
//    @PostMapping()
//    public String create(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult) {
//        // TODO
//
//        /*        bookValidator.validate(book, bindingResult);*/
//        if (bindingResult.hasErrors())
//            return "products/new";
//
//        productsService.save(product);
//        return "redirect:/products";
//    }
//
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") int id) {
//        productsService.delete(id);
//
//        return "redirect:/products";
//    }
//
//    // TODO
//
///*    @PatchMapping("/{id}/release")
//    public String release(@PathVariable("id") int id) {
//        productsService.release(id);
//
//        return "redirect:/products/" + id;
//    }
//
//    @PatchMapping("/{id}/assign")
//    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
//        productsService.assign(id, selectedPerson);
//
//        return "redirect:/products/" + id;
//    }*/
//
//    @GetMapping("/search")
//    public String searchPage() {
//        return "products/search";
//    }
//
//    @PostMapping("/search")
//    public String makeSearch(Model model, @RequestParam("query") String query) {
//        model.addAttribute("products", productsService.searchByProductName(query));
//
//        return "products/search";
//    }
//}
