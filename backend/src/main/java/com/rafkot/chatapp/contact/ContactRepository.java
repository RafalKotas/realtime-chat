package com.rafkot.chatapp.contact;

import com.rafkot.chatapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {

    List<Contact> findAllByOwner(User owner);

    Optional<Contact> findByIdAndOwner(UUID id, User owner);
}
