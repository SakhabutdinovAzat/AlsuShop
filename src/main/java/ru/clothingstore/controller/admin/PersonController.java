package ru.clothingstore.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.person.Person;
import ru.clothingstore.service.OrderService;
import ru.clothingstore.service.PersonService;
import ru.clothingstore.service.RoleService;
import ru.clothingstore.util.PersonValidator;

import javax.validation.Valid;
import java.util.*;

@Controller(value = "adminUserController")
@RequestMapping("/admin/user")
public class PersonController {

    private final PersonService personService;
    private final PersonValidator personValidator;
    private final OrderService orderService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public PersonController(PersonService personService, PersonValidator personValidator, OrderService orderService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.personValidator = personValidator;
        this.orderService = orderService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public String index(Model model) {

        List<Person> users = new ArrayList<>(personService.findAll());
        // Отсортируем по ID
        users.sort(Comparator.comparing(Person::getId));
        model.addAttribute("users", personService.findAll());

        return "admin/user/index";
    }

    // TODO
    @GetMapping("/add")
    public String add (Model model) {
        getUserModel(model, new Person());
        return "admin/user/add";
    }

    @PostMapping("/add")
    public String add (@ModelAttribute("user") @Valid Person person, BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "admin/user/add";

        person.setCart(new Cart());
        //if admin don't select role
        if (person.getRole() == null) {
            person.setRole(roleService.getRoleByName("ROLE_USER"));
        }
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personService.save(person);
        redirectAttrs.addFlashAttribute("success", "User added: " + person);
        return "redirect:/admin/user";
    }

    @GetMapping(value = {"/update/{id}", "/update"})
    public String edit(Model model, @PathVariable("id") int id, RedirectAttributes redirectAttrs) {
        if (id == 0) {
            redirectAttrs.addFlashAttribute("error", "Not the selected user");
            return "redirect:/admin/user";
        }

        getUserModel(model, personService.findOne(id));
        return "admin/user/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid Person user, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            getUserModel(model, user);
            return "admin/user/update";
        }
        personService.update(user);
        redirectAttrs.addFlashAttribute("success", "User updated: " + user);
        return "redirect:/admin/user";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttrs) {
        Person person = personService.findOne(id);
        System.out.println("before");
        personService.delete(id);
        System.out.println("after");
        redirectAttrs.addFlashAttribute("success", "Delete user: " + person);
        return "redirect:/admin/user";
    }

    private Model getUserModel(Model model, Person person) {
        model.addAttribute("currentRoles", person.getRole());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("user", person);
        return model;
    }

    // TODO
//    @InitBinder("user")
//    protected void initBinder(WebDataBinder binder) {
//        binder.setValidator(userFormValidation);
//    }
}
