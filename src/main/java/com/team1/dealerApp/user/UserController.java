package com.team1.dealerApp.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("userId") UUID id) throws NoSuchElementException {
            UserDTO userDTO = userService.getUserDTOById(id);
            log.debug("User with id: {} found", id);
            return ResponseEntity.status(HttpStatus.FOUND).body(userDTO);
    }

    @GetMapping("/u/users/details")
    public ResponseEntity<UserDTO> getUserDetails(@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok(userService.getUserDetails(user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@AuthenticationPrincipal UserDetails user, @RequestBody CreateUserDTO createUserDTO) throws NoSuchElementException{
            return ResponseEntity.ok(userService.updateUser(user, createUserDTO));
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(userService.deleteUser(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

}
