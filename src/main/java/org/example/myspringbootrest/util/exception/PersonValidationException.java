package org.example.myspringbootrest.util.exception;

import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class PersonValidationException extends RuntimeException {

    public PersonValidationException(String message) {
        super(message);
    }

    public PersonValidationException(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg
                        .append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }
            throw new PersonValidationException(errorMsg.toString());
        }
    }
}