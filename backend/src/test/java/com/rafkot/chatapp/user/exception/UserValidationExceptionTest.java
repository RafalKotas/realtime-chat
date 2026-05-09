package com.rafkot.chatapp.user.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserValidationExceptionTest {

    UserValidationException subject;

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        Map<String, String> errors = new HashMap<>();

        // when
        errors.put("email", "Email bob123@mail.com is already taken");
        errors.put("username", "Username bob123 is already taken");
        subject = new  UserValidationException(httpStatus, errors);

        // then
        assertNotNull(subject);
        assertThat(subject.getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(subject.getErrors()).isNotNull();
        assertThat(subject.getErrors().get("email")).isNotNull().contains("Email bob123@mail.com is already taken");
        assertThat(subject.getErrors().get("username")).isNotNull().contains("Username bob123 is already taken");
    }
}