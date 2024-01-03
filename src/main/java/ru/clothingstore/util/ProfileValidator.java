package ru.clothingstore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.clothingstore.model.user.Profile;
import ru.clothingstore.model.user.User;
import ru.clothingstore.service.UserService;

@Component
public class ProfileValidator implements Validator {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileValidator(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Profile.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Profile profile = (Profile) o;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.getByUsername(userName).orElseGet(User::new);

        if (!profile.getPasswordActual().isEmpty() && passwordEncoder.encode(profile.getPasswordActual()).equals(user.getPassword())) {
            errors.rejectValue("passwordActual", "", "Password is wrong");
        }

        if (profile.getPassword1() != null && !profile.getPassword1().equals(profile.getPassword2())) {
            errors.rejectValue("password2", "", "Passwords is different");
        }

        if (!user.getEmail().equals(profile.getEmail()) && userService.getByEmail(profile.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already taken");
        }
    }
}
