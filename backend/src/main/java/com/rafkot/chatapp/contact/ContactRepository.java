package com.rafkot.chatapp.contact;

import com.rafkot.chatapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {

    List<Contact> findAllByOwner(User owner);

    Optional<Contact> findByIdAndOwner(UUID id, User owner);

    @Query("""
       SELECT COUNT(*)
       FROM Contact c
       WHERE (c.owner.username = :username OR c.member.username = :username)
   """)
    int countUserContacts(String username);

    @Query("""
        SELECT
                CASE
                        WHEN COUNT(c) > 0 THEN TRUE
                        ELSE FALSE
                END
        FROM Contact c 
        WHERE (c.member.id = :memberId AND c.owner.id =:memberId) OR (c.member.id = :ownerId AND c.owner.id =:ownerId)
    """)
    boolean existByOwnerAndMember(UUID ownerId, UUID memberId);

    boolean existsByOwnerIdAndMemberId(UUID ownerId, UUID memberId);
}
