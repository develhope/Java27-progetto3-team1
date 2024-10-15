package com.team1.dealerApp.admin;

import org.springframework.stereotype.Component;

    
@Component
public class AdminMapper {

    public Admin toAdmin (AdminDTO adminDTO ){
        return Admin.builder()
                .firstName(adminDTO.getFirstName())
                .lastName(adminDTO.getLastName())
                .email(adminDTO.getEmail())
                .phoneNumber(adminDTO.getPhoneNumber())
                .build();
    }

    public AdminDTO toAdminDTO ( Admin admin ){
        return AdminDTO.builder()
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .email(admin.getEmail())
                .phoneNumber(admin.getPhoneNumber())
                .build();
    }


    public Admin fromCreateAdminDTOToAdmin ( CreateAdminDTO createAdminDTO ) {
        return Admin.builder()
                .firstName(createAdminDTO.getFirstName())
                .lastName(createAdminDTO.getLastName())
                .email(createAdminDTO.getEmail())
                .phoneNumber(createAdminDTO.getPhoneNumber())
                .build();
    }

}
