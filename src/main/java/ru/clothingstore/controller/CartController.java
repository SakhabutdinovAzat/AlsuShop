package ru.clothingstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.good.Good;
import ru.clothingstore.model.user.User;
import ru.clothingstore.model.product.Product;
import ru.clothingstore.service.CartService;
import ru.clothingstore.service.GoodService;
import ru.clothingstore.service.UserService;

import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final UserService userService;
    private final GoodService goodService;
    private final CartService cartService;

    @Autowired
    public CartController(UserService userService, GoodService goodService, CartService cartService) {
        this.userService = userService;
        this.goodService = goodService;
        this.cartService = cartService;
    }

    @GetMapping()
    public String show(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getByUsername(username).orElseGet(User::new);
        model.addAttribute("products", user.getCart().getProducts());
        model.addAttribute("cart", user.getCart());

        return "cart/index";
    }

    @ResponseBody
    @PostMapping("/delete/{id}")
    public Set<Product> deleteProduct(@PathVariable("id") int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getByUsername(username).orElseGet(User::new);

        Cart cart = user.getCart();
        Product product = cart.getProduct(id);

        Double productSum = product.getGood().getPrice() * product.getCount();
        cart.setSum(cart.getSum() - productSum);


        if (cart.getProducts().remove(product)){
            cartService.updateCart(cart);}

        return user.getCart().getProducts();
    }

    @ResponseBody
    @PostMapping(value = {"/calculate"})
    public Map<String, String> cartCalculate(@RequestBody Map<String, String> json) {

        int id = Integer.parseInt(json.get("id"));
        boolean isPlus =  Boolean.parseBoolean(json.get("plus"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.getByUsername(userName).orElseGet(User::new);
        Cart cart = user.getCart();
        Product product = cart.getProduct(id);

        if (isPlus) {
            product.setCount(product.getCount() + 1);
            cart.setSum(cart.getSum() + product.getGood().getPrice());
        } else {
            if (product.getCount() > 1) {
                product.setCount(product.getCount() - 1);
                cart.setSum(cart.getSum() - product.getGood().getPrice());
            }
        }
        cartService.updateCart(cart);

        Map<String, String> objects = new HashMap<>();
        objects.put("count", String.valueOf(product.getCount()));
        objects.put("sum", String.valueOf(cart.getSum()));

        return objects;
    }

    @ResponseBody
    @PostMapping(value = {"/buy/{id}"})
    public ResponseEntity<Good> buyGood(@PathVariable("id") int id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getByUsername(username).orElseGet(User::new);
        Good good = goodService.getGoodById(id, true);

        Cart cart = user.getCart();

        addItemInCart(good, cart);
        cartService.updateCart(cart);

        return new ResponseEntity<>(good, HttpStatus.OK);
    }

    private void addItemInCart(Good good, Cart cart) {
        Set<Product> products = cart.getProducts();
        boolean flag = true;
        for (Product product : products) {
            if (product.getGood().equals(good)) {
                product.setCount(product.getCount() + 1);
                cart.setSum(cart.getSum() + good.getPrice());
                product.setAddedAt(new Date());
                flag = false;
            }
        }
        if (flag) {
            Product newProduct = new Product();
            newProduct.setGood(good);
            newProduct.setCart(cart);
            newProduct.setCount(1);
            newProduct.setAddedAt(new Date());
            // TODO поменять price у product на double
            newProduct.setPrice((int) Math.round(good.getPrice()));
            newProduct.setName(good.getTitle());
            cart.setSum(cart.getSum() + good.getPrice());
            cart.addProduct(newProduct);
        }
    }
}
