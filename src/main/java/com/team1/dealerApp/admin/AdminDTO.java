package com.team1.dealerApp.admin;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
}
