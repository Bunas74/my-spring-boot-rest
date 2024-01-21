package org.example.myspringbootrest;

import org.example.myspringbootrest.repository.PeopleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class BaseJUnitTest {

    @Mock
    protected PeopleRepository peopleRepository;

    @Mock
    protected PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}