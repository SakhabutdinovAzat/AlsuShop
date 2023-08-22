package ru.clothingstore.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.person.Person;
import ru.clothingstore.model.person.Reputation;
import ru.clothingstore.repository.PersonRepository;
import ru.clothingstore.service.PersonService;
import ru.clothingstore.service.RoleService;

import java.util.Date;
import java.util.UUID;

@Service
public class RegistrationService {
    private final PersonRepository personRepository;
    private final RoleService roleService;
    private final PersonService personService;
    private final MailServiceImpl mailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PersonRepository personRepository, RoleService roleService, PersonService personService, MailServiceImpl mailService, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.roleService = roleService;
        this.personService = personService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
        person.setRole(roleService.getRoleByName("ROLE_USER"));
        person.setActive(true);
        person.setCreatedAt(new Date());
        person.setCart(new Cart());
        person.setReputation(Reputation.NORMAL);
        person.setActivationCode(UUID.randomUUID().toString());
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);

        if (!person.getEmail().isEmpty()) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welkome to ClothingStore. Please, visit next link: http://localhost:8083/auth/activate/%s",
                    person.getUsername(),
                    person.getActivationCode()
            );

            mailService.sendEmail(person.getEmail(), "Activation code", message);
        }
    }

}
