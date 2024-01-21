package org.example.myspringbootrest.util.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PersonErrorResponse {

    private String message;

    private LocalDateTime timestamp;
}