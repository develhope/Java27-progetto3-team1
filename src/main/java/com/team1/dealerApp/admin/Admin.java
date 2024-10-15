package com.team1.dealerApp.admin;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Setter;

import java.util.UUID;

import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

        @Id
        @GeneratedValue
        private UUID id;

        @Setter
        @Column(name = "first_name", nullable = false)
        private String firstName;

        @Setter
        @Column(name = "last_name", nullable = false)
        private String lastName;

        @Setter
        @Column(nullable = false)
        private String email;

        @Setter
        @Column(name = "phone_number", length = 10)
        private String phoneNumber;

        @Setter
        @Column(nullable = false)
        private String password;

        public void setId(UUID id) {
        }
}
