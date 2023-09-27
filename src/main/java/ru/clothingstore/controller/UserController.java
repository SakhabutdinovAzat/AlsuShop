package ru.clothingstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clothingstore.model.person.User;
import ru.clothingstore.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName()).orElse(null);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "user/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Principal principal,
                                @RequestParam String password,
                                @RequestParam String email) {
        userService.updateProfile(principal, password, email);

        return "redirect:/user/profile";
    }
}
