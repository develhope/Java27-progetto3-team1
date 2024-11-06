package com.team1.dealerApp.user;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.team1.dealerApp.paypal.PayPalService;
import com.team1.dealerApp.subscription.Subscription;
import com.team1.dealerApp.subscription.SubscriptionMapper;
import com.team1.dealerApp.subscription.SubscriptionService;
import com.team1.dealerApp.subscription.SubscriptionType;
import com.team1.dealerApp.utils.Pager;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserService {
    private static final String USER_EMAIL_ERROR = "There is no user with email ";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;
    private final PayPalService payPalService;
    private final Pager pager;


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
        boolean isActive = false;
        User userToDelete = userRepository.findByEmail(user.getUsername()).orElseThrow(()-> new NoSuchElementException("There is no user with email " + user.getUsername()));
        userToDelete.setActive(isActive);
        userToDelete.getSubscriptions().forEach(s->s.setStatus(isActive));
        userRepository.save(userToDelete);
        return true;
    }

    public boolean deleteUser(UUID id) {
        Boolean isActive = false;
        User userToDelete = userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("There is no user with id " + id ));
        userToDelete.setActive(isActive);
        userToDelete.getSubscriptions().forEach(s-> s.setStatus(isActive));
        userRepository.save(userToDelete);
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

    public Page <UserDTO> getAllUser(int page, int size) {
        Page<User> allUser = userRepository.findAll(pager.createPageable(page,size));
        return allUser.map(userMapper::toUserDTO);
    }

    public UserDTO getUserDetails(UserDetails user) {
        User userFound = userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException(USER_EMAIL_ERROR + user.getUsername()));
        return userMapper.toUserDTO(userFound);
    }

    public User getUserByEmail(UserDetails user) {
        return userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException(USER_EMAIL_ERROR + user.getUsername()));
    }

	public String updateSubscriptionPlan( UserDetails user, String subscription ) throws NoSuchElementException, PayPalRESTException {
        String email = user.getUsername();
        User updatable = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(USER_EMAIL_ERROR + email));

        SubscriptionType subscriptionType = SubscriptionType.valueOf(subscription.toUpperCase());
        updatable.getSubscriptions().removeIf(s -> s.getSubscriptionType().equals(subscriptionType));
        Subscription newSubscription = new Subscription();
        newSubscription.setSubscriptionType(subscriptionType);
        newSubscription.setUsers(updatable);
        newSubscription.setPrice(20.00);
        newSubscription.setStartDate(LocalDate.now());
        newSubscription.setEndDate(LocalDate.now().plusDays(30));
        subscriptionService.addSubscription(newSubscription);
        updatable.getSubscriptions().add(newSubscription);

        Payment payment = payPalService.createPayment(newSubscription.getPrice(), "EUR", "paypal", "sale", "Subscription");

        userRepository.save(updatable);

        return payment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel()))
                .findFirst()
                .map(Links::getHref)
                .orElse(null);
    }

    public UserDTO updateSubscriptionEndDate(UserDetails user, Long subscriptionId, LocalDate date) throws NoSuchElementException{
        subscriptionService.updateSubscriptionEndDate(subscriptionId, date);
        User userFound = userRepository.findByEmail(user.getUsername()).orElseThrow(()-> new NoSuchElementException(USER_EMAIL_ERROR + user.getUsername()));
        return userMapper.toUserDTO(userFound);
    }

    public UserDTO deleteSubscription(UserDetails user, Long subscriptionId) {
        subscriptionService.getSubscriptionDetails(subscriptionId);
        User userFound = userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new NoSuchElementException(USER_EMAIL_ERROR + user.getUsername()));
        return userMapper.toUserDTO(userFound);
    }
}
