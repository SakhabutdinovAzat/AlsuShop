package ru.clothingstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.Person;
import ru.clothingstore.model.Reputation;
import ru.clothingstore.repository.PeopleRepository;

import java.util.Date;

@Service
public class RegistrationService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
/*        person.setAddress("Dfdfdf, Dfdfdf, 555555");
        person.setFirstName("Ffff");
        person.setLastName("Llll");
        person.setAge(34);*/
        person.setRole("ROLE_USER");
        person.setCreatedAt(new Date());
        person.setReputation(Reputation.NORMAL);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }

}
