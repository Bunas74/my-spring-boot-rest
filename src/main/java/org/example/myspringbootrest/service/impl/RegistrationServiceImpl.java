package org.example.myspringbootrest.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.myspringbootrest.model.Person;
import org.example.myspringbootrest.repository.PeopleRepository;
import org.example.myspringbootrest.service.RegistrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;

    public void registration(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setRole("ROLE_USER");
        person.setCreatedWho("registration");
        peopleRepository.save(person);
    }
}