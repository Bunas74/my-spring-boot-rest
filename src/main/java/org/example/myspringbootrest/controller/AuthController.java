package org.example.myspringbootrest.controller;

import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.myspringbootrest.dto.AuthDTO;
import org.example.myspringbootrest.model.Person;
import org.example.myspringbootrest.security.JWTUtil;
import org.example.myspringbootrest.service.impl.RegistrationServiceImpl;
import org.example.myspringbootrest.util.PersonValidator;
import org.example.myspringbootrest.util.exception.PersonValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationServiceImpl registrationService;

    private final PersonValidator personValidator;

    private final ModelMapper modelMapper;

    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PersonValidationException(bindingResult);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("Error","Incorrect credentials"));
        }
        String token = jwtUtil.generateToken(authDTO.getUsername());

        return ResponseEntity.ok(Map.of("JWT-token", token));
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> createNewUser(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        Person person = modelMapper.map(authDTO, Person.class);

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new PersonValidationException(bindingResult);
        }
        registrationService.registration(person);

        String token = jwtUtil.generateToken(person.getUsername());

        return ResponseEntity.ok(Map.of("JWT-token", token));
    }
}