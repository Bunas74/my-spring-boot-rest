package org.example.myspringbootrest.service;

import java.util.List;
import java.util.Optional;
import org.example.myspringbootrest.model.Person;

public interface PeopleService {

    List<Person> findAll();

    Person findOne(Long id);

    Optional<Person> findByUsername(String username);
}