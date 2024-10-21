package com.team1.dealerApp.auth;

import com.team1.dealerApp.config.JwtService;
import com.team1.dealerApp.user.Role;
import com.team1.dealerApp.user.SubscriptionStatus;
import com.team1.dealerApp.user.User;
import com.team1.dealerApp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register( RegisterRequest request ) throws BadRequestException {
		if(userRepository.existsByEmail(request.getEmail())){
			throw new BadRequestException("Email " + request.getEmail() + " is already associated to a user");
		}
		User user = User.builder()
				.firstName( request.getFirstName() )
				.lastName( request.getLastName() )
				.email( request.getEmail() )
				.password(passwordEncoder.encode(request.getPassword()))
				.phoneNumber(request.getPhoneNumber())
				.subscriptionStatus(SubscriptionStatus.NOT_SUBSCRIBED)
				.role(Role.USER)
				.build();

		userRepository.save( user );

		String token = jwtService.generateToken( user );

		return AuthenticationResponse.builder()
				.token( token )
				.build();
	}

	public AuthenticationResponse authenticate( AuthenticationRequest request ) throws NoSuchElementException{

		authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()) );

		User user = userRepository.findByEmail( request.getEmail() )
				.orElseThrow(() -> new NoSuchElementException("No user with email " + request.getEmail()));

		String token = jwtService.generateToken( user );

		return AuthenticationResponse.builder()
				.token( token )
				.build();

	}
}
