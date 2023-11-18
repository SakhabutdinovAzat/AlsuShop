package ru.clothingstore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        User user = userService.getByUsername(username).orElse(null);
        model.addAttribute("products", user.getCart().getProducts());
        model.addAttribute("cart", user.getCart());

        return "cart/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getByUsername(username).orElse(null);

        Cart cart = user.getCart();
        Product product = cart.getProduct(id);

        Double productSum = product.getGood().getPrice() * product.getCount();
        cart.setSum(cart.getSum() - productSum);


        if (cart.getProducts().remove(product)){
            cartService.updateCart(cart);}
        return "redirect:/cart";
    }

    // TODO реализовать
    @ResponseBody
    @RequestMapping(value = {"/calculate"})
    public String cartCalculate(@RequestBody Map<String, String> json) {

        int id = Integer.valueOf(json.get("id"));
        boolean isPlus = Boolean.valueOf(json.get("isPlus"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.getByUsername(userName).orElse(null);
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

        Map<String, Object> objects = new HashMap<>();
        objects.put("count", product.getCount());
        objects.put("sum", cart.getSum());

        return getJson(objects);
    }

    @PostMapping(value = {"/buy/{id}"})
    public String buyGood(@PathVariable("id") int id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getByUsername(username).orElse(null);
        Good good = goodService.getGoodById(id, true);

        Cart cart = user.getCart();

        addItemInCart(good, cart);
        cartService.updateCart(cart);

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getName();
//
//        User user = personService.findByUsername(userName).orElse(null);
//        Good good = goodService.getGoodById(Integer.valueOf(id), true);
//
//        Cart cart = user.getCart();
//        addItemInCart(good, cart);
//        cartService.updateCart(cart);

        // Todo релизовать или убрать
//        return getJson("<b>" + good.getTitle() + "</b> been successfully added in your cart!");
        return "redirect:/index";
    }

    // Метод для преобразования Java объекта в JavaScript объект или строку
    private String getJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Метод добавляет Product в указанную корзину
    private void addItemInCart(Good good, Cart cart) {
        Set<Product> products = cart.getProducts();
        boolean flag = true;
        for (Product product : products) {
            if (product.getGood().equals(good)) {
                product.setCount(product.getCount() + 1);
                cart.setSum(cart.getSum() + good.getPrice());
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
