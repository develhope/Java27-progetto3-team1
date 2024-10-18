package com.team1.dealerApp.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;


    @PostMapping()
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody CreateAdminDTO createAdminDTO) throws BadRequestException {
        log.debug("Admin created: {}", createAdminDTO);
        return ResponseEntity.ok(adminService.createAdmin(createAdminDTO));
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable("adminId") UUID adminId) throws NoSuchElementException {
        log.debug("Getting admin with Id {}", adminId);
        return ResponseEntity.ok(adminService.getAdminById(adminId));
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<Boolean> deleteAdmin(@PathVariable("adminId") UUID adminId) {
        log.debug("Deleted admin with Id {}", adminId);
        return ResponseEntity.ok(adminService.deleteAdmin(adminId));
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable("adminId") UUID adminId, @RequestBody CreateAdminDTO createAdminDTO) throws NoSuchElementException {
        log.debug("Updated with Id {}", adminId);
        return ResponseEntity.ok(adminService.updateAdmin(adminId, createAdminDTO));
    }


}
