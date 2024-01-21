package org.example.myspringbootrest.unit;

import java.util.Collections;
import java.util.Optional;
import org.example.myspringbootrest.BaseJUnitTest;
import org.example.myspringbootrest.model.Person;
import org.example.myspringbootrest.service.impl.PeopleServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PeopleServiceTest extends BaseJUnitTest {

    @InjectMocks
    private PeopleServiceImpl peopleService;


    @Test
    void findAll() {
        when(peopleRepository.findAll()).thenReturn(Collections.emptyList());

        peopleService.findAll();

        verify(peopleRepository, times(1)).findAll();
    }

    @Test
    void findOne() {
        Person person = new Person();
        person.setId(1L);

        when(peopleRepository.findById(anyLong())).thenReturn(Optional.of(person));

        peopleService.findOne(1L);

        verify(peopleRepository, times(1)).findById(1L);
    }

    @Test
    void findByUsername() {
        Person person = new Person();
        person.setUsername("username");

        when(peopleRepository.findByUsername(anyString())).thenReturn(Optional.of(person));

        peopleService.findByUsername("username");

        verify(peopleRepository, times(1)).findByUsername("username");
    }
}