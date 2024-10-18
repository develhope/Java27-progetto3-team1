package com.team1.dealerApp.user;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserDTO getUserDTOById(UUID id) throws NoSuchElementException {
        User getUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with Id " + id + " not found"));
        return userMapper.toUserDTO(getUser);
    }

    public UserDTO updateUser(UUID id, CreateUserDTO createUserDTO) throws NoSuchElementException {

        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("This User doesn't exist");
        }

        User updateUser = userMapper.toUser(createUserDTO);
        updateUser.setId(id);
        userRepository.save(updateUser);

        return userMapper.toUserDTO(updateUser);
    }

    public boolean deleteUser(UUID id) {
        userRepository.deleteById(id);
        return true;
    }


    public User registerUser(CreateUserDTO userDTO) throws BadRequestException {
        User user = userMapper.toUser(userDTO);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        // Crittografia della password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserById(UUID id) throws NoSuchElementException {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No User with Id " + id));

    }

}
