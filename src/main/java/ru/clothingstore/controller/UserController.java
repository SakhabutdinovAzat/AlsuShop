package ru.clothingstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.clothingstore.model.user.Profile;
import ru.clothingstore.model.user.User;
import ru.clothingstore.service.UserService;

import javax.validation.Valid;
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
        User user = userService.getByUsername(principal.getName()).orElseGet(User::new);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("profile", new Profile());

        return "user/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Principal principal,
                                @ModelAttribute("profile") @Valid Profile profile,
                                BindingResult bindingResult) {

        if (bindingResult.hasErrors() && !profile.getEmail().isEmpty()) {
            return "user/profile";
        }

        userService.updateProfile(principal, profile);


        return "redirect:/user/profile";
    }
}
