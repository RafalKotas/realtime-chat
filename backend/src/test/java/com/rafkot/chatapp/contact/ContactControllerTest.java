package com.rafkot.chatapp.contact;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@WebMvcTest(ContactController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContactControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private ContactService contactService;

    @Test
    void shouldGetUserContacts() {
        // given
        ContactDto firstContactDto = new ContactDto(
                UUID.fromString("11111-22222-33333-44444-11111"),
                "firstTestContact",
                null
        );
        ContactDto secondContactDto = new ContactDto(
                UUID.fromString("11111-22222-33333-44444-22222"),
                "secondTestContact",
                null
        );
        List<ContactDto> contactDtoList = new ArrayList<>(List.of(firstContactDto, secondContactDto));
        when(contactService.getContactsForUser("testUser")).thenReturn(contactDtoList);

        // when + then
        assertThat(mockMvcTester.get()
                .uri("/api/contacts")
                .param("username", "testUser")
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo("""
                    [
                        {
                          "username": "firstTestContact",
                          "id": "00011111-2222-3333-4444-000000011111"
                        },
                                                {
                          "username": "secondTestContact",
                          "id": "00011111-2222-3333-4444-000000022222"
                        }
                    ]
                """);
    }

    @Test
    void shouldReturnNotFoundResponseIfContactsAreEmpty() {
        // given
        when(contactService.getContactsForUser("testUser")).thenReturn(Collections.emptyList());

        // when + then
        assertThat(mockMvcTester.get()
                .uri("/api/contacts")
                .param("username", "testUser")
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldDeleteContactIfCorrectlyAuthenticated() {
        // given
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "testUser")
                .build();

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        UUID contactId = UUID.fromString("11111111-2222-3333-4444-555555555555");

        // when + then
        assertThat(mockMvcTester.delete()
                .uri("/api/contacts/{id}", contactId)
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.NO_CONTENT);

        verify(contactService).deleteContact(contactId, "testUser");
    }

    @Test
    void shouldReturn401WhenJwtIsNull() {
        // given
        UUID contactId = UUID.fromString("11111111-2222-3333-4444-555555555555");

        // when + then
        assertThat(mockMvcTester.delete()
                .uri("/api/contacts/{id}", contactId)
                .accept(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.UNAUTHORIZED)
                .bodyJson()
                .isLenientlyEqualTo("""
                {
                  "authentication": "username is null"
                }
            """);
    }

    @Test
    void shouldReturn401WhenJwtHasNoSubClaim() {
        // given
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("testClaim", "testValue")
                .build();

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(auth);

        UUID contactId = UUID.fromString("11111111-2222-3333-4444-555555555555");

        // when + then
        assertThat(mockMvcTester.delete()
                .uri("/api/contacts/{id}", contactId)
                .accept(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.UNAUTHORIZED)
                .bodyJson()
                .isLenientlyEqualTo("""
                {
                  "authentication": "username is null"
                }
            """);
    }

    @Test
    void shouldReturn404WhenContactNotFound() {
        // given
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "testUser")
                .build();

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        UUID contactId = UUID.fromString("11111111-2222-3333-4444-555555555555");

        doThrow(new ContactNotFoundException(contactId))
                .when(contactService)
                .deleteContact(contactId, "testUser");

        // when + then
        assertThat(mockMvcTester.delete()
                .uri("/api/contacts/{id}", contactId)
                .accept(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson()
                .isLenientlyEqualTo("""
                {
                  "contact": "Contact not found: 11111111-2222-3333-4444-555555555555"
                }
            """);
    }
}