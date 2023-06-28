package ru.alsushop.AlsuShop.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alsushop.AlsuShop.model.Person;
import ru.alsushop.AlsuShop.service.PeopleService;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

/*        // посмотреть, если человек с таким ФИО в БД
        if(peopleService.findByLastnameAndFirstname(person.getFirstName()).isPresent())
            errors.rejectValue("fullName", "", "This fullName id already taken");*/

        // посмотреть, если человек с таким email в БД
        if (peopleService.findByEmail(person.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already taken");
        }

        if(peopleService.findByUsername(person.getUsername()).isPresent()){
            errors.rejectValue("username", "", "Username is already exist");
        }

/*        // Неверный формат даты
        if(person.getDateOfBirth())  {
            errors.rejectValue("dateOfBirth", "", "Date of birth invalid");
        }*/
    }
}
