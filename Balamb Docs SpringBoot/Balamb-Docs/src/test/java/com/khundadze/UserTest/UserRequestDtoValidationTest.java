package com.khundadze.UserTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.khundadze.Balamb_Docs.dtos.UserRequestDto;

public class UserRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void invalidNameShouldFailValidation() {
        UserRequestDto dto = new UserRequestDto("", "luka@example.com", "password123");
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validDtoShouldPassValidation() {
        UserRequestDto dto = new UserRequestDto("Luka", "luka@example.com", "password123");
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
