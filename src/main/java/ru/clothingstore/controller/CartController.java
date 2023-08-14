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
import ru.clothingstore.model.person.Person;
import ru.clothingstore.model.product.Product;
import ru.clothingstore.service.CartService;
import ru.clothingstore.service.GoodService;
import ru.clothingstore.service.PersonService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final PersonService personService;
    private final GoodService goodService;
    private final CartService cartService;

    @Autowired
    public CartController(PersonService personService, GoodService goodService, CartService cartService) {
        this.personService = personService;
        this.goodService = goodService;
        this.cartService = cartService;
    }

    @GetMapping()
    public String show(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Person person = personService.findByUsername(username).orElse(null);
        model.addAttribute("products", person.getCart().getProducts());
        model.addAttribute("cart", person.getCart());

        return "cart/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Person person = personService.findByUsername(username).orElse(null);
        Cart cart = person.getCart();
        Product product = cart.getProduct(id);

        Double productSum = product.getGood().getPrice() * product.getCount();
        cart.setSum(cart.getSum() - productSum);


        if (cart.getProducts().remove(product))
            cartService.updateCart(cart);

        return "redirect:/cart";
    }

    @ResponseBody
    @RequestMapping(value = {"/calculate"})
    public String cartCalculate(@RequestBody Map<String, String> json) {

        Integer id = Integer.valueOf(json.get("id"));
        Boolean isPlus = Boolean.valueOf(json.get("isPlus"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Person person = personService.findByUsername(userName).orElse(null);
        Cart cart = person.getCart();
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

    @ResponseBody
    @RequestMapping(value = {"/buy"})
    public String buyGood(@RequestBody String id) {

        System.out.println("Enter buy");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Person person = personService.findByUsername(userName).orElse(null);
        Good good = goodService.getGoodById(Integer.valueOf(id), true);

        Cart cart = person.getCart();
        addItemInCart(good, cart);
        cartService.updateCart(cart);

        System.out.println("Before return buy");
        return getJson("<b>" + good.getTitle() + "</b> been successfully added in your cart!");
    }

    // Метод для преобразования Java объекта в JavaScript объект или строку
    private String getJson(Object object) {
        System.out.println("Enter getJson");
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.out.println("In catch getJson");
            e.printStackTrace();
        }
        System.out.println("Before return getJson");
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
            cart.setSum(cart.getSum() + good.getPrice());
            cart.addProduct(newProduct);
        }
    }
}
