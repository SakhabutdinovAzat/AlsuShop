package ru.alsushop.AlsuShop.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alsushop.AlsuShop.model.Order;
import ru.alsushop.AlsuShop.model.Person;
import ru.alsushop.AlsuShop.repository.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);

        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatePerson) {
        Person personToBeUpdate = peopleRepository.findById(id).get();
        updatePerson.setUsername(personToBeUpdate.getUsername());
        updatePerson.setPassword(personToBeUpdate.getPassword());
        updatePerson.setReputation(personToBeUpdate.getReputation());
        updatePerson.setCreatedAt(personToBeUpdate.getCreatedAt());
        updatePerson.setRole(personToBeUpdate.getRole());
        updatePerson.setId(id);
        peopleRepository.save(updatePerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> findByLastnameAndFirstname(String lastName, String firstName) {
        return peopleRepository.findByLastNameAndFirstName(lastName, firstName);
    }

    public Optional<Person> findByEmail(String email) {
        return peopleRepository.findByEmail(email);
    }

    public Optional<Person> findByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    public List<Order> getOrdersById(int id) {
        Optional<Person> person = peopleRepository.findById(id);

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
