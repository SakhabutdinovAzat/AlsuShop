package ru.alsushop.AlsuShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alsushop.AlsuShop.model.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByLastNameAndFirstName(String lastName, String firstName);

    Optional<Person> findByEmail(String email);

    Optional<Person> findByUsername(String username);
}
