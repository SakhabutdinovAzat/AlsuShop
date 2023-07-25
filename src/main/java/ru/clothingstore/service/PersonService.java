package ru.clothingstore.service;

import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    public List<Person> findAll();

    public Person findOne(int id);

    public void save(Person person);

    public void update(int id, Person updatePerson);

    public void delete(int id);

    public Optional<Person> findByLastnameAndFirstname(String lastName, String firstName);

    public Optional<Person> findByEmail(String email);

    public Person findByUsername(String username);

    public List<Order> getOrdersById(int id);
}
