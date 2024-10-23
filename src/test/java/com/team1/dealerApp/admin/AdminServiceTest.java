package com.team1.dealerApp.admin;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AdminServiceTest {
    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AdminMapper adminMapper;

    @InjectMocks
    private AdminService adminService;

    private Admin admin;
    private AdminDTO adminDTO;
    private CreateAdminDTO createAdminDTO;
    private UUID adminId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminId = UUID.randomUUID();

        admin = new Admin();
        admin.setFirstName("Mario");
        admin.setLastName("Rossi");
        admin.setPassword("mariorossi");
        admin.setPhoneNumber("0000");
        admin.setId(adminId);
        admin.setEmail("admin@example.com");

        adminDTO = new AdminDTO();
        adminDTO.setId(admin.getId());
        adminDTO.setEmail("admin@example.com");
        adminDTO.setFirstName(admin.getFirstName());
        adminDTO.setLastName(admin.getLastName());
        adminDTO.setPhoneNumber(admin.getPhoneNumber());

        createAdminDTO = new CreateAdminDTO();
        createAdminDTO.setEmail("admin@example.com");
        createAdminDTO.setPassword("mariorossi");
    }

    @Test
    void testGetAdminById_ThrowsNoSuchElementException() {
        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adminService.getAdminById(adminId));
    }

    @Test
    void testGetAdminById() {

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(adminMapper.toAdminDTO(admin)).thenReturn(adminDTO);

        AdminDTO result = adminService.getAdminById(adminId);

        assertEquals(adminId, result.getId());
    }

    @Test
    void testUpdateAdmin_ThrowsEntityNotFoundException() {
        when(adminRepository.existsById(adminId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> adminService.updateAdmin(adminId, createAdminDTO));
    }

    @Test
    void testUpdateAdmin() {
        when(adminRepository.existsById(adminId)).thenReturn(true);
        when(adminMapper.toAdmin(createAdminDTO)).thenReturn(admin);
        when(adminMapper.toAdminDTO(admin)).thenReturn(adminDTO);

        AdminDTO result = adminService.updateAdmin(adminId, createAdminDTO);
        assertEquals(adminId, result.getId());
    }

    @Test
    void testDeleteAdmin_Successful() {
        boolean result = adminService.deleteAdmin(adminId);
        assertTrue(result);
    }
}

