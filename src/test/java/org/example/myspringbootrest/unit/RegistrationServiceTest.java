package org.example.myspringbootrest.unit;

import org.example.myspringbootrest.BaseJUnitTest;
import org.example.myspringbootrest.model.Person;
import org.example.myspringbootrest.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegistrationServiceTest extends BaseJUnitTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    void registration() {
        Person person = new Person();
        person.setPassword("password");
        person.setRole("ROLE_USER");
        person.setCreatedWho("registration");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        registrationService.registration(person);

        verify(passwordEncoder, times(1)).encode("password");
        verify(peopleRepository, times(1)).save(any(Person.class));
    }
}