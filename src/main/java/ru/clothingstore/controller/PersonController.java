package ru.clothingstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clothingstore.model.person.Person;
import ru.clothingstore.service.PersonService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal) {
        Person person = personService.findByUsername(principal.getName()).orElse(null);
        model.addAttribute("username", person.getUsername());
        model.addAttribute("email", person.getEmail());

        return "user/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Principal principal,
                                @RequestParam String password,
                                @RequestParam String email) {
        personService.updateProfile(principal, password, email);

        return "redirect:/user/profile";
    }
}
