package com.team1.dealerApp.admin;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
}
