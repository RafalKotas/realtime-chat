package com.rafkot.chatapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {

    private UserDetailsImpl subject;

    @Test
    void shouldInstantiateAndRetrieveValues() {
        // given + when
        subject = createTestUserDetailsImpl();

        // then
        assertThat(subject).isNotNull();
        assertThat(subject.getPassword()).isEqualTo("password");
        assertThat(subject.getUsername()).isEqualTo("username");
        assertThat(subject.getEmail()).isEqualTo("email");
        assertThat(subject.getAuthorities()).isEmpty();
        assertThat(subject.isAccountNonExpired()).isTrue();
        assertThat(subject.isAccountNonLocked()).isTrue();
        assertThat(subject.isCredentialsNonExpired()).isTrue();
        assertThat(subject.isEnabled()).isTrue();
    }

    @Test
    void equalsShouldReturnFalseWhenComparingSameObject() {
        // given
        subject = createTestUserDetailsImpl();

        // when
        boolean result = subject.equals(subject);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsNull() {
        // given
        subject = createTestUserDetailsImpl();
        UserDetailsImpl nullUserDetailsImpl = null;

        // when
        boolean result = subject.equals(nullUserDetailsImpl);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void equalsShouldReturnFalseWhenComparingDifferentObjects() {
        // given
        subject = createTestUserDetailsImpl();
        String userDetailsImpl = "userDetailsImpl";

        // when
        boolean result = subject.equals(userDetailsImpl);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void equalsShouldReturnFalseWhenOtherUserDetailsImplHasDifferentId() {
        // given
        subject = createTestUserDetailsImpl();
        UserDetailsImpl userDetailsImpl = createTestUserDetailsImpl();


        // when
        boolean result = subject.equals(userDetailsImpl);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void equalsShouldReturnTrueWhenOtherUserDetailsImplHasSameId() {
        // given
        subject = createTestUserDetailsImpl();
        UserDetailsImpl userDetailsImpl;
        userDetailsImpl = new UserDetailsImpl(
                subject.getId(),
                "strongpass",
                "test-uname",
                "email@email.com",
                new HashSet<>(),
                true,
                false,
                false,
                true
        );


        // when
        boolean result = subject.equals(userDetailsImpl);

        // then
        assertThat(result).isTrue();
    }


    private UserDetailsImpl createTestUserDetailsImpl() {
        UUID uuid = UUID.randomUUID();
        String password = "password";
        String username = "username";
        String email = "email";
        Set<GrantedAuthority> authorities = new HashSet<>();
        boolean accountNonExpired = true;
        boolean accountNonLocked = true;
        boolean credentialsNonExpired = true;
        boolean enabled = true;
        return new UserDetailsImpl(
                uuid,
                password,
                username,
                email,
                authorities,
                accountNonExpired,
                accountNonLocked,
                credentialsNonExpired,
                enabled
        );
    }

    @Test
    void hashCodeShouldDependOnlyOnId() {
        // given
        UUID id = UUID.fromString("11111111-2222-3333-4444-555555555555");
        
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(id)
                .username("userA")
                .email("a@mail.com")
                .password("passA")
                .authorities(Set.of())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(id)
                .username("userB")
                .email("b@mail.com")
                .password("passB")
                .authorities(Set.of())
                .accountNonExpired(false)
                .accountNonLocked(false)
                .credentialsNonExpired(false)
                .enabled(false)
                .build();

        UserDetailsImpl user3 = UserDetailsImpl.builder()
                .id(UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"))
                .username("userC")
                .email("c@mail.com")
                .password("passC")
                .authorities(Set.of())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
        
        // when
        int user1Hash = user1.hashCode();
        int user2Hash = user2.hashCode();
        int user3Hash = user3.hashCode();

        // then
        assertThat(user1Hash)
                .isEqualTo(user2Hash)
                .isNotEqualTo(user3Hash);
    }
}