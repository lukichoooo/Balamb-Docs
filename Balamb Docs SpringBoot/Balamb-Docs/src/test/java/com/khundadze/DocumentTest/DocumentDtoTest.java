package com.khundadze.DocumentTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class DocumentDtoTest {

    @Test
    public void validDtoShouldPassValidation() {
        DocumentRequestDto dto = new DocumentRequestDto("name", "description", "content");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void invalidNameShouldFailValidation() {
        DocumentRequestDto dto = new DocumentRequestDto("", "description", "content");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
