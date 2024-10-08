package com.team1.dealerApp.services;

import com.team1.dealerApp.entities.User;
import com.team1.dealerApp.mappers.UserMapper;
import com.team1.dealerApp.models.dtos.CreateUserDTO;
import com.team1.dealerApp.models.dtos.UserDTO;
import com.team1.dealerApp.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;

import java.util.UUID;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO createUser(CreateUserDTO createUserDTO) throws Exception {

        if (createUserDTO.getEmail() == null || createUserDTO.getPassword() == null) {
            throw new BadRequestException("Either Email or Password is null");
        }

        User newUser = userMapper.fromCreateUserDTOToUser(createUserDTO);
        userRepository.save(newUser);

        return userMapper.toUserDTO(newUser);
    }

    public UserDTO getUserById(UUID id) throws Exception {
        User getUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with Id " + id + " not found"));
        return userMapper.toUserDTO(getUser);
    }

    public UserDTO updateUser(UUID id, CreateUserDTO createUserDTO) throws Exception {

        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("This User doesn't exist");
        }

        User updateUser = userMapper.fromCreateUserDTOToUser(createUserDTO);
        updateUser.setId(id);
        userRepository.save(updateUser);

        return userMapper.toUserDTO(updateUser);
    }

    public boolean deleteUser(UUID id) {
        userRepository.deleteById(id);
        return true;
    }

}
