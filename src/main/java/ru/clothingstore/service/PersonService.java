package ru.clothingstore.service;

import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.Person;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface PersonService {
    public List<Person> findAll();

    public Person findOne(int id);

    public void save(Person person);

    public void update(Person updatePerson);

    public void delete(int id);

    public Optional<Person> findByLastnameAndFirstname(String lastName, String firstName);

    public Optional<Person> findByEmail(String email);

    public Optional<Person> findByUsername(String username);

    public List<Order> getOrdersById(int id);

    boolean activateUser(String code);

    void updateProfile(Principal principal, String password, String email);
}
