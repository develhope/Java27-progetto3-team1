package com.team1.dealerApp.admin;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public AdminDTO createAdmin(CreateAdminDTO createAdminDTO) throws BadRequestException {

        if (createAdminDTO.getEmail() == null || createAdminDTO.getPassword() == null) {
            throw new BadRequestException("Either Email or Password is null");
        }

        Admin newAdmin = adminMapper.fromCreateAdminDTOToAdmin(createAdminDTO);
        adminRepository.save(newAdmin);

        return adminMapper.toAdminDTO(newAdmin);
    }

    public AdminDTO getAdminById(UUID id) throws Exception {
        Admin getAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin with Id " + id + " not found"));
        return adminMapper.toAdminDTO(getAdmin);
    }

    public AdminDTO updateAdmin(UUID id, CreateAdminDTO createAdminDTO) throws NoSuchElementException {

        if (!adminRepository.existsById(id)) {
            throw new EntityNotFoundException("This Admin doesn't exist");
        }

        Admin updateAdmin = adminMapper.fromCreateAdminDTOToAdmin(createAdminDTO);
        updateAdmin.setId(id);
        adminRepository.save(updateAdmin);

        return adminMapper.toAdminDTO(updateAdmin);
    }

    public boolean deleteAdmin(UUID id) {
        adminRepository.deleteById(id);
        return true;
    }

}

