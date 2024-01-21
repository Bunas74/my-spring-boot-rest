package org.example.myspringbootrest.repository;

import java.util.Optional;
import org.example.myspringbootrest.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUsername(String username);
}