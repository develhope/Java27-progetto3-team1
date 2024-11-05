package com.team1.dealerApp.auth;

import com.team1.dealerApp.config.JwtService;
import com.team1.dealerApp.user.Role;
import com.team1.dealerApp.user.User;
import com.team1.dealerApp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

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
				.role(Role.ROLE_USER)
				.isActive(true)
				.build();

		userRepository.save( user );

		String token = jwtService.generateToken( user );

		return AuthenticationResponse.builder()
				.token( token )
				.build();
	}

	public AuthenticationResponse authenticate( AuthenticationRequest request ) throws NoSuchElementException{
	String token;
		authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()) );
		Optional<User> found = userRepository.findByEmail( request.getEmail() );
		if(found.isPresent()){
			User user = found.get();

			 token = jwtService.generateToken( user );
			return AuthenticationResponse.builder()
					.token( token )
					.build();

		}
		throw new NoSuchElementException("User with email " + request.getEmail() + " doesn't exist");
	}

	public AuthenticationResponse registerAdmin(RegisterRequest request) throws BadRequestException{
		if(userRepository.existsByEmail(request.getEmail())){
			throw new BadRequestException("Email " + request.getEmail() + " is already associated to a user");
		}
		User user = User.builder()
				.firstName( request.getFirstName() )
				.lastName( request.getLastName() )
				.email( request.getEmail() )
				.password(passwordEncoder.encode(request.getPassword()))
				.phoneNumber(request.getPhoneNumber())
				.role(Role.ROLE_ADMIN)
				.isActive(true)
				.build();

		userRepository.save( user );

		String token = jwtService.generateToken( user );

		return AuthenticationResponse.builder()
				.token( token )
				.build();
	}

}
