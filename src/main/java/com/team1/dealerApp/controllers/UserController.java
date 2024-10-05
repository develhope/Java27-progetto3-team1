package com.team1.dealerApp.controllers;

import com.team1.dealerApp.models.dtos.CreateUserDTO;
import com.team1.dealerApp.models.dtos.UserDTO;
import com.team1.dealerApp.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<?> createUser ( @RequestBody CreateUserDTO createUserDTO ) throws Exception {

        try {
            UserDTO userDTO = userService.createUser(createUserDTO);
            log.debug("User added in database {}", userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (Exception e){
            log.error("Error in add new User {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

}
