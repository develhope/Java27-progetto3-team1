package com.team1.dealerApp.user;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserDTO getUserDTOById(UUID id) throws NoSuchElementException {
        User getUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with Id " + id + " not found"));
        return userMapper.toUserDTO(getUser);
    }

    public UserDTO updateUser(UserDetails user, CreateUserDTO createUserDTO) throws NoSuchElementException {
        Optional<User> userFound = userRepository.findByEmail(user.getUsername());

        if (userFound.isEmpty()) {
            throw new NoSuchElementException("This User doesn't exist");
        }

        User updateUser = userMapper.toUser(createUserDTO);
        updateUser.setId(userFound.get().getId());
        userRepository.save(updateUser);

        return userMapper.toUserDTO(updateUser);
    }

    public boolean deleteUser(UserDetails user) {
        userRepository.deleteByEmail(user.getUsername());
        return true;
    }

    public boolean deleteUser(UUID id) {
        userRepository.deleteById(id);
        return true;
    }


    public UserDTO registerUser(CreateUserDTO userDTO) throws BadRequestException {
        User user = userMapper.toUser(userDTO);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        // Crittografia della password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toUserDTO(user);
    }

    public User getUserById(UUID id) throws NoSuchElementException {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No User with Id " + id));

    }

    public List<UserDTO> getAllUser() {
        List<User> allUser = userRepository.findAll();
        return allUser.stream().map(userMapper::toUserDTO).toList();
    }

    public UserDTO getUserDetails(UserDetails user) {
        User userFound = userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException("No users with email: " + user.getUsername()));
        return userMapper.toUserDTO(userFound);
    }

    public User getUserByEmail(UserDetails user) {
        return userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException("No users with email: " + user.getUsername()));
    }

	public UserDTO updateSubscriptionPlan( UserDetails user, String subscription ) throws BadRequestException {
        String email = user.getUsername();
        User updatable = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No user with email " + email));
        switch (subscription.toLowerCase().trim()){
            case "tvshow" -> updatable.setRole(Role.ROLE_TVSHOWS);
            case "movie" -> updatable.setRole(Role.ROLE_MOVIES);
            case "none" -> updatable.setRole(Role.ROLE_USER);
            default -> throw new BadRequestException("No such plan: " + subscription );
        }
        userRepository.save(updatable);
        return userMapper.toUserDTO(updatable);
    }
}
