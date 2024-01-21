package org.example.myspringbootrest.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.myspringbootrest.model.Person;
import org.example.myspringbootrest.repository.PeopleRepository;
import org.example.myspringbootrest.service.PeopleService;
import org.example.myspringbootrest.util.exception.PersonNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private final PeopleRepository peopleRepository;

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(Long id) {
        return peopleRepository.findById((id))
                .orElseThrow(() -> new PersonNotFoundException("Человека с таким id не существует"));
    }

    public Optional<Person> findByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }
}