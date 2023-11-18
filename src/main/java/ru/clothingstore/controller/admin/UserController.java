package ru.clothingstore.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.user.User;
import ru.clothingstore.service.OrderService;
import ru.clothingstore.service.RoleService;
import ru.clothingstore.service.UserService;
import ru.clothingstore.util.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller(value = "adminUserController")
@RequestMapping("/admin/user")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final OrderService orderService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserController(UserService userService, UserValidator userValidator, OrderService orderService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.orderService = orderService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public String index(Model model) {

        List<User> users = new ArrayList<>(userService.getAll());
        // Отсортируем по ID
        users.sort(Comparator.comparing(User::getId));
        model.addAttribute("users", userService.getAll());

        return "admin/user/index";
    }

    // TODO
    @GetMapping("/add")
    public String add (Model model) {
        getUserModel(model, new User());
        return "admin/user/add";
    }

    @PostMapping("/add")
    public String add (@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return "admin/user/add";

        user.setCart(new Cart());
        //if admin don't select role
        if (user.getRole() == null) {
            user.setRole(roleService.getRoleByName("ROLE_USER"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        redirectAttrs.addFlashAttribute("success", "User added: " + user);
        return "redirect:/admin/user";
    }

    @GetMapping(value = {"/update/{id}", "/update"})
    public String edit(Model model, @PathVariable("id") int id, RedirectAttributes redirectAttrs) {
        if (id == 0) {
            redirectAttrs.addFlashAttribute("error", "Not the selected user");
            return "redirect:/admin/user";
        }

        getUserModel(model, userService.getOne(id));
        return "admin/user/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            getUserModel(model, user);
            return "admin/user/update";
        }
        userService.update(user);
        redirectAttrs.addFlashAttribute("success", "User updated: " + user);
        return "redirect:/admin/user";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttrs) {
        User user = userService.getOne(id);
        System.out.println("before");
        userService.delete(id);
        System.out.println("after");
        redirectAttrs.addFlashAttribute("success", "Delete user: " + user);
        return "redirect:/admin/user";
    }

    private Model getUserModel(Model model, User user) {
        model.addAttribute("currentRoles", user.getRole());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("user", user);
        return model;
    }

    // TODO
//    @InitBinder("user")
//    protected void initBinder(WebDataBinder binder) {
//        binder.setValidator(userFormValidation);
//    }
}
