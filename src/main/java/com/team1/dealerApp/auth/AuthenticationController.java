package com.team1.dealerApp.auth;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity< AuthenticationResponse > register ( @RequestBody RegisterRequest request ) throws BadRequestException {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> register ( @RequestBody AuthenticationRequest request ){
		return ResponseEntity.ok(authService.authenticate(request));
	}

}
