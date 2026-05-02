package com.rafkot.chatapp.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Test
    void whenUserValidationException_thenReturnProperStatus() {
        assertThat(mockMvcTester.perform(get("/api/user/me")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)))
                .hasStatus(HttpStatus.UNAUTHORIZED)
                .hasBodyTextEqualTo("{\"authentication\":\"username is null\"}");
    }
}