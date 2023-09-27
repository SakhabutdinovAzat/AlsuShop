package ru.clothingstore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.clothingstore.model.person.User;
import ru.clothingstore.service.UserService;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        // сравнить введеные пароли
        if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
            errors.rejectValue("password2", "", "Passwords is different");
        }

        // посмотреть, если человек с таким email в БД
        if (userService.getByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already taken");
        }

        if(userService.getByUsername(user.getUsername()).isPresent()){
            errors.rejectValue("username", "", "Username is already exist");
        }

        // TODO реализовать

        /*  // посмотреть, если человек с таким ФИО в БД
        if(userService.findByLastnameAndFirstname(user.getFirstName()).isPresent())
            errors.rejectValue("fullName", "", "This fullName id already taken");*/

/*        // Неверный формат даты
        if(user.getDateOfBirth())  {
            errors.rejectValue("dateOfBirth", "", "Date of birth invalid");
        }*/
    }
}
