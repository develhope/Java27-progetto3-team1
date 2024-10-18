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

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) throws BadRequestException{
            UserDTO userDTO = userService.createUser(createUserDTO);
            log.debug("User added in database {}", userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("userId") UUID id) throws NoSuchElementException {
            UserDTO userDTO = userService.getUserDTOById(id);
            log.debug("User with id: {} found", id);
            return ResponseEntity.status(HttpStatus.FOUND).body(userDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") UUID id, @RequestBody CreateUserDTO createUserDTO) throws NoSuchElementException{
            return ResponseEntity.ok(userService.updateUser(id, createUserDTO));
    }


    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(@PathVariable("userId") UUID id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

}
