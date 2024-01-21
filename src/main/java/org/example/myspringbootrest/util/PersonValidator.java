package org.example.myspringbootrest.util;


import java.util.Optional;
import org.example.myspringbootrest.model.Person;
import org.example.myspringbootrest.service.impl.PeopleServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleServiceImpl peopleService;

    public PersonValidator(PeopleServiceImpl peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> existsPerson = peopleService.findByUsername(person.getUsername());
        if (existsPerson.isPresent()) {
            errors.rejectValue("username", "", "Человек с таким именем уже существует");
        }
    }
}