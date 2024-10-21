package com.team1.dealerApp.admin;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    public AdminDTO registerAdmin(CreateAdminDTO createAdminDTO) throws BadRequestException {
        Admin admin = adminMapper.toAdmin(createAdminDTO);
        if (adminRepository.existByEmail(createAdminDTO.getEmail())) {
            throw new BadRequestException("Either Email or Password is null");
        }
        admin.setPassword(passwordEncoder.encode(createAdminDTO.getPassword()));
        adminRepository.save(admin);
        return adminMapper.toAdminDTO(admin);
    }

    public AdminDTO getAdminById(UUID id) throws NoSuchElementException{
        Admin getAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Admin with Id " + id + " not found"));
        return adminMapper.toAdminDTO(getAdmin);
    }

    public AdminDTO updateAdmin(UUID id, CreateAdminDTO createAdminDTO) throws NoSuchElementException {

        if (!adminRepository.existsById(id)) {
            throw new EntityNotFoundException("This Admin doesn't exist");
        }

        Admin updateAdmin = adminMapper.toAdmin(createAdminDTO);
        updateAdmin.setId(id);
        adminRepository.save(updateAdmin);

        return adminMapper.toAdminDTO(updateAdmin);
    }

    public boolean deleteAdmin(UUID id) {
        adminRepository.deleteById(id);
        return true;
    }



}

