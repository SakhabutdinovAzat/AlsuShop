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
import ru.clothingstore.service.PersonService;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
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
            personToBeUpdate.setPassword(updatePerson.getPassword());
            personToBeUpdate.setActive(updatePerson.getActive());
            personToBeUpdate.setEmail(updatePerson.getEmail());
            personToBeUpdate.setRole(updatePerson.getRole());
//        updatePerson.setUsername(personToBeUpdate.getUsername());
//        updatePerson.setPassword(personToBeUpdate.getPassword());
//            updatePerson.setReputation(personToBeUpdate.getReputation());
//            updatePerson.setCreatedAt(personToBeUpdate.getCreatedAt());
//            updatePerson.setCart(personToBeUpdate.getCart());
//            updatePerson.setId(personToBeUpdate.getId());
//            updatePerson.setReputation(personToBeUpdate.getReputation());
//        updatePerson.setRole(personToBeUpdate.getRole());
            System.out.println("before save");
            personRepository.save(personToBeUpdate);
            System.out.println("after save");
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
}
