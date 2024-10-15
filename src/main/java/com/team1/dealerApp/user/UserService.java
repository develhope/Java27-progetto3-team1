package com.team1.dealerApp.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
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

    public UserDTO getUserDTOById(UUID id) throws Exception {
        User getUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with Id " + id + " not found"));
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

    public User getUserById(UUID id) throws NoSuchElementException{
        return userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("No User with Id " + id));
    }

}
