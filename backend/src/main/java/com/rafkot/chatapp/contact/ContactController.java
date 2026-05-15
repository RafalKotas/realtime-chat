package com.rafkot.chatapp.contact;

import com.rafkot.chatapp.user.exception.UserValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController (ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<ContactDto>> getContacts(@RequestParam String username) {
        List<ContactDto> userContacts = contactService.getContactsForUser(username);

        return userContacts.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(userContacts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null || jwt.getClaim("sub") == null) {
            throw new UserValidationException(HttpStatus.UNAUTHORIZED,
                    Map.of("authentication", "username is null"));
        }

        String username = jwt.getClaim("sub");

        contactService.deleteContact(id, username);

        return ResponseEntity.noContent().build();
    }

}
