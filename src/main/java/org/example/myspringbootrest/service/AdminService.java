package org.example.myspringbootrest.service;

import org.example.myspringbootrest.dto.AdminDTO;
import org.example.myspringbootrest.model.Person;

public interface AdminService {

    void createPerson(Person person);

    void updatePerson(Long id, AdminDTO updatePerson);

    void deletePerson(Long id);
}