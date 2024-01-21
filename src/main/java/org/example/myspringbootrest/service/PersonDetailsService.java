package org.example.myspringbootrest.service;

import lombok.RequiredArgsConstructor;
import org.example.myspringbootrest.repository.PeopleRepository;
import org.example.myspringbootrest.security.PersonDetails;
import org.example.myspringbootrest.util.exception.PersonNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new PersonDetails(peopleRepository.findByUsername(username)
                .orElseThrow(() -> new PersonNotFoundException("Человека с таким именем не существует")));
    }
}