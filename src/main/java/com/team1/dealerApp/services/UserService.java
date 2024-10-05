package com.team1.dealerApp.services;

import com.team1.dealerApp.entities.User;
import com.team1.dealerApp.mappers.UserMapper;
import com.team1.dealerApp.models.dtos.CreateUserDTO;
import com.team1.dealerApp.models.dtos.UserDTO;
import com.team1.dealerApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO createUser(CreateUserDTO createUserDTO) throws Exception{

        if(createUserDTO.getEmail() == null || createUserDTO.getPassword() == null){
            throw new Exception("Either Email or Password is null");
        }

        User newUser = userMapper.fromCreateUserDTOToUser(createUserDTO);
        userRepository.save(newUser);

        return userMapper.toUserDTO(newUser);
    }
}
