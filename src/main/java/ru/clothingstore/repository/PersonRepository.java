package ru.clothingstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clothingstore.model.person.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByLastNameAndFirstName(String lastName, String firstName);

    Optional<Person> findByEmail(String email);

    Optional<Person> findByUsername(String username);

    Person findByActivationCode(String code);
}
