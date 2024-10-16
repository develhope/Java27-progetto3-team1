package com.team1.dealerApp.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateUserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") UUID id) {
        try {
            UserDTO userDTO = userService.getUserById(id);
            log.debug("User whti id: {} found", id);
            return ResponseEntity.status(HttpStatus.FOUND).body(userDTO);
        } catch (Exception e) {
            log.error("Error in getting User by Id: {} - {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") UUID id, @RequestBody CreateUserDTO createUserDTO) throws Exception {
        try {
            return ResponseEntity.ok(userService.updateUser(id, createUserDTO));
        } catch (Exception e) {
            log.error("Error in update User whit Id: {} - {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping
    public ResponseEntity deleteUser(@PathVariable("userId") UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.status(200).build();
    }

}
