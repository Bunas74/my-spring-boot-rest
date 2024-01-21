package org.example.myspringbootrest.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.myspringbootrest.dto.AdminDTO;
import org.example.myspringbootrest.model.Person;
import org.example.myspringbootrest.repository.PeopleRepository;
import org.example.myspringbootrest.service.AdminService;
import org.example.myspringbootrest.util.exception.PersonNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;

    public void createPerson(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setCreatedWho(SecurityContextHolder.getContext().getAuthentication().getName());
        peopleRepository.save(person);
    }

    public void updatePerson(Long id, AdminDTO updatePerson) {
        Person person = peopleRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Человека с таким id не существует"));
        person.setUsername(updatePerson.getUsername());
        person.setPassword(passwordEncoder.encode(updatePerson.getPassword()));
        person.setRole(updatePerson.getRole());
        person.setUpdatedAt(LocalDateTime.now());
        peopleRepository.save(person);
    }

    public void deletePerson(Long id) {
        Person person = peopleRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Человека с таким id не существует"));
        peopleRepository.delete(person);
    }
}