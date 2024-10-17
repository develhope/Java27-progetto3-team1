package com.team1.dealerApp.admin;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;


    @PostMapping()
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody CreateAdminDTO createAdminDTO) throws BadRequestException{
            return ResponseEntity.ok(adminService.createAdmin(createAdminDTO));
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable ("adminId") UUID adminId ) throws NoSuchElementException{
            return ResponseEntity.ok(adminService.getAdminById(adminId));
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<Boolean> deleteAdmin(@PathVariable ("adminId") UUID adminId){
        return ResponseEntity.ok(adminService.deleteAdmin(adminId));
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable ("adminId") UUID adminId, @RequestBody CreateAdminDTO createAdminDTO) throws NoSuchElementException{
            return ResponseEntity.ok(adminService.updateAdmin(adminId,createAdminDTO));
    }






}
