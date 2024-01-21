package org.example.myspringbootrest.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.myspringbootrest.model.Person;
import org.example.myspringbootrest.service.impl.PeopleServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/people")
@RequiredArgsConstructor
public class PeopleController {

    private final PeopleServiceImpl peopleService;

    @GetMapping
    public List<Person> getPeople() {
        return peopleService.findAll();
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") Long id) {
        return peopleService.findOne(id);
    }
}