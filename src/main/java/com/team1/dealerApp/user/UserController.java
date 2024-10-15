package com.team1.dealerApp.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody CreateUserDTO createUserDTO){

        try {
            UserDTO userDTO = userService.createUser(createUserDTO);
            log.debug("User added in database {}", userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (BadRequestException e) {
            log.error("Error in add new User {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }


    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") UUID id) {
        try {
            UserDTO userDTO = userService.getUserDTOById(id);
            log.debug("User with id: {} found", id);
            return ResponseEntity.status(HttpStatus.FOUND).body(userDTO);
        } catch (NoSuchElementException e) {
            log.error("Error in getting User by Id: {} - {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") UUID id, @RequestBody CreateUserDTO createUserDTO){
        try {
            return ResponseEntity.ok(userService.updateUser(id, createUserDTO));
        } catch (NoSuchElementException e) {
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
