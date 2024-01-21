package org.example.myspringbootrest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDTO {

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    private String username;

    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

    @NotEmpty(message = "Роль не должна быть пустой")
    private String role;
}