package ru.clothingstore.service.Impl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.cart.Cart;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.Person;
import ru.clothingstore.model.person.Reputation;
import ru.clothingstore.repository.PersonRepository;
import ru.clothingstore.service.MailService;
import ru.clothingstore.service.PersonService;

import java.security.Principal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = personRepository.findById(id);

        return foundPerson.orElse(null);
    }

    @Override
    @Transactional
    public void save(Person person) {
        person.setReputation(Reputation.NORMAL);
        person.setCreatedAt(new Date());
        person.setCart(new Cart());
        person.setActive(true);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
    }

    // TODO
    @Override
    @Transactional
    public void update(Person updatePerson) {
        Optional<Person> optionalPerson = personRepository.findById(updatePerson.getId());
        if (optionalPerson.isPresent()) {
            Person personToBeUpdate = optionalPerson.get();
            personToBeUpdate.setUsername(updatePerson.getUsername());
            personToBeUpdate.setActive(updatePerson.getActive());
            personToBeUpdate.setEmail(updatePerson.getEmail());
            personToBeUpdate.setRole(updatePerson.getRole());

            if (personToBeUpdate.getPassword().equals(updatePerson.getPassword())) {
                personToBeUpdate.setPassword(updatePerson.getPassword());
            } else {
                personToBeUpdate.setPassword(passwordEncoder.encode(updatePerson.getPassword()));
            }
//        updatePerson.setUsername(personToBeUpdate.getUsername());
//        updatePerson.setPassword(personToBeUpdate.getPassword());
//            updatePerson.setReputation(personToBeUpdate.getReputation());
//            updatePerson.setCreatedAt(personToBeUpdate.getCreatedAt());
//            updatePerson.setCart(personToBeUpdate.getCart());
//            updatePerson.setId(personToBeUpdate.getId());
//            updatePerson.setReputation(personToBeUpdate.getReputation());
//        updatePerson.setRole(personToBeUpdate.getRole());
            personRepository.save(personToBeUpdate);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    @Override
    public Optional<Person> findByLastnameAndFirstname(String lastName, String firstName) {
        return personRepository.findByLastNameAndFirstName(lastName, firstName);
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    @Override
    public Optional<Person> findByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    @Override
    // TODO
    public List<Order> getOrdersById(int id) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isPresent()){
            Hibernate.initialize(person.get().getOrders());

            // Проверка просрочености оплаты
            person.get().getOrders().forEach(order -> {
                long diffInMillies = Math.abs(order.getOrderDate().getTime() - new Date().getTime());
                // 2592000000 миллисекунд = 3 суток
                if (diffInMillies >= 259200000) {
                    order.setExpired(true); // оплата просрочена
                }
            });
            return person.get().getOrders();
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public boolean activateUser(String code) {
        Person person = personRepository.findByActivationCode(code);
        if (person == null) {
            return false;
        }
        person.setActivationCode(null);

        personRepository.save(person);
        return true;
    }

    @Transactional
    @Override
    public void updateProfile(Principal principal, String password, String email) {
        Person person = personRepository.findByUsername(principal.getName()).get();
        String userEmail = person.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) || (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            person.setEmail(email);

            if (!email.isEmpty()) {
                person.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if (!password.isEmpty()) {
            person.setPassword(passwordEncoder.encode(password));
        }

        personRepository.save(person);
        if (isEmailChanged) {
            sendMessage(person);
        }
    }


    private void sendMessage(Person person) {
        if (!person.getEmail().isEmpty()) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welkome to ClothingStore. You are change your profile. Please, visit next link: http://localhost:8083/auth/activate/%s",
                    person.getUsername(),
                    person.getActivationCode()
            );

            mailService.sendEmail(person.getEmail(), "Activation code", message);
        }
    }
}
