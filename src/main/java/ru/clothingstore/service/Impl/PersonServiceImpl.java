package ru.clothingstore.service.Impl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.order.Order;
import ru.clothingstore.model.person.Person;
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

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = personRepository.findById(id);

        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatePerson) {
        Person personToBeUpdate = personRepository.findById(id).get();
        updatePerson.setUsername(personToBeUpdate.getUsername());
        updatePerson.setPassword(personToBeUpdate.getPassword());
        updatePerson.setReputation(personToBeUpdate.getReputation());
        updatePerson.setCreatedAt(personToBeUpdate.getCreatedAt());
        updatePerson.setRole(personToBeUpdate.getRole());
        updatePerson.setId(id);
        personRepository.save(updatePerson);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    public Optional<Person> findByLastnameAndFirstname(String lastName, String firstName) {
        return personRepository.findByLastNameAndFirstName(lastName, firstName);
    }

    public Optional<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public Person findByUsername(String username) {
        return personRepository.findByUsername(username).orElse(null);
    }

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
