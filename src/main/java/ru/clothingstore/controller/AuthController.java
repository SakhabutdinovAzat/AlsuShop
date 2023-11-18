package ru.clothingstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.clothingstore.model.dto.CaptchaResponseDto;
import ru.clothingstore.model.user.User;
import ru.clothingstore.service.RegistrationService;
import ru.clothingstore.service.UserService;
import ru.clothingstore.util.UserValidator;

import javax.validation.Valid;
import java.util.Collections;

@Controller
@RequestMapping("/auth")
@Tag(name = "Контроллер авторизации", description = "Регистрация и авторизация пользователей")
public class AuthController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final UserService userService;
    private final RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    public AuthController(RegistrationService registrationService, UserValidator userValidator, UserService userService, RestTemplate restTemplate) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.userService = userService;
        this.restTemplate = restTemplate;
    }


    @GetMapping("/login")
    @Operation(summary = "Страница авторизации")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    @Operation(summary = "Страница регистрации")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Регистрация пользователя", description = "Позволяет зарегистрировать пользователя")
    public String performRegistration(@RequestParam("g-recaptcha-response") @Parameter(description = "Ответ от recaptcha") String captchaResponse,
                                      @ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
            return "auth/registration";
        }

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        registrationService.register(user);
        redirectAttributes.addFlashAttribute("success", "You are registered! Please log in! Your login: " + user.getUsername());
        return "redirect:/auth/login";
    }

    @GetMapping("/activate/{code}")
    @Operation(summary = "Активация email пользователя")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "alert alert-success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "alert alert-danger");
            model.addAttribute("message", "Activation code is not found");
        }
        return "auth/login";
    }

    // TODO
    @GetMapping("/403")
    public String accessDenied(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            model.addAttribute("username", userDetail.getUsername());
        }

        return "404";
    }


}
