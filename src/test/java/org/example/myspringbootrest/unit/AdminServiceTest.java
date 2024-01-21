package org.example.myspringbootrest.unit;

import java.util.Optional;
import org.example.myspringbootrest.BaseJUnitTest;
import org.example.myspringbootrest.dto.AdminDTO;
import org.example.myspringbootrest.model.Person;
import org.example.myspringbootrest.service.impl.AdminServiceImpl;
import org.example.myspringbootrest.util.exception.PersonNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminServiceTest extends BaseJUnitTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void createPerson() {
        Person person = new Person();
        person.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("admin");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(peopleRepository.save(any(Person.class))).thenReturn(person);

        adminService.createPerson(person);

        verify(passwordEncoder, times(1)).encode("password");
        verify(peopleRepository, times(1)).save(any(Person.class));
    }

    @Test
    void updatePerson_WithExistingId_UpdatesPerson() {
        Person person = new Person();
        AdminDTO updatePerson = new AdminDTO();
        updatePerson.setUsername("newUsername");
        updatePerson.setPassword("newPassword");
        updatePerson.setRole("newRole");

        when(peopleRepository.findById(1L)).thenReturn(Optional.of(person));
        when(passwordEncoder.encode(updatePerson.getPassword())).thenReturn("encodedPassword");

        adminService.updatePerson(1L, updatePerson);

        verify(peopleRepository, times(1)).save(person);
        assertEquals("newUsername", person.getUsername());
        assertEquals("encodedPassword", person.getPassword());
        assertEquals("newRole", person.getRole());
         assertNotNull(person.getUpdatedAt());
    }

    @Test
    void updatePerson_WithNonExistingId_ThrowsException() {
        AdminDTO updatePerson = new AdminDTO();

        when(peopleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> adminService.updatePerson(999L, updatePerson));

        verify(peopleRepository, never()).save(any());
    }

    @Test
    void deletePerson_WithExistingId_DeletesPerson() {
        Person person = new Person();
        when(peopleRepository.findById(1L)).thenReturn(Optional.of(person));

        adminService.deletePerson(1L);

        verify(peopleRepository, times(1)).delete(person);
    }

    @Test
    void deletePerson_WithNonExistingId_ThrowsException() {
        when(peopleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> adminService.deletePerson(999L));

        verify(peopleRepository, never()).delete(any());
    }
}