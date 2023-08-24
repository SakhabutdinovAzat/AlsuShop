package ru.clothingstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
import ru.clothingstore.model.person.Person;
import ru.clothingstore.service.Impl.RegistrationService;
import ru.clothingstore.service.PersonService;
import ru.clothingstore.util.PersonValidator;

import javax.validation.Valid;
import java.util.Collections;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    private final RegistrationService registrationService;
    private final PersonValidator personValidator;
    private final PersonService personService;
    private final RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator personValidator, PersonService personService, RestTemplate restTemplate) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.personService = personService;
        this.restTemplate = restTemplate;
    }


    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@RequestParam("g-recaptcha-response") String captchaResponse,
                                      @ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
            return "auth/registration";
        }

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        registrationService.register(person);
        redirectAttributes.addFlashAttribute("success", "You are registered! Please log in! Your login: " + person.getUsername());
        return "redirect:/auth/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = personService.activateUser(code);

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
