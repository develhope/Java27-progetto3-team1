package com.team1.dealerApp.admin;

import com.team1.dealerApp.user.Role;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Admin implements UserDetails {

        @Id
        @Setter
        @GeneratedValue
        private UUID id;

        @Setter
        @Column(name = "first_name", nullable = false)
        private String firstName;

        @Setter
        @Column(name = "last_name", nullable = false)
        private String lastName;

        @Setter
        @Column(nullable = false, unique = true)
        private String email;

        @Setter
        @Column(name = "phone_number", length = 10)
        private String phoneNumber;

        @Setter
        @Column(nullable = false)
        private String password;

        @Enumerated(EnumType.STRING)
        @Setter
        @Column(nullable = false)
        private Role role;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
        }

        @Override
        public String getUsername() {
                return email;
        }
}
