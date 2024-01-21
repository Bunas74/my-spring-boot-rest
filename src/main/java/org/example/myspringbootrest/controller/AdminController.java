package org.example.myspringbootrest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.myspringbootrest.dto.AdminDTO;
import org.example.myspringbootrest.model.Person;
import org.example.myspringbootrest.service.impl.AdminServiceImpl;
import org.example.myspringbootrest.util.PersonValidator;
import org.example.myspringbootrest.util.exception.PersonValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/admin")
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AdminController {

    private final AdminServiceImpl adminService;

    private final ModelMapper modelMapper;

    private final PersonValidator personValidator;

    @PostMapping
    public ResponseEntity<HttpStatus> createPerson(@RequestBody @Valid AdminDTO adminDTO, BindingResult bindingResult) {
        Person person = modelMapper.map(adminDTO, Person.class);

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new PersonValidationException(bindingResult);
        }
        adminService.createPerson(person);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updatePerson(@RequestBody @Valid AdminDTO adminDTO, @PathVariable("id") Long id,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PersonValidationException(bindingResult);
        }
        adminService.updatePerson(id, adminDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") Long id) {
        adminService.deletePerson(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}