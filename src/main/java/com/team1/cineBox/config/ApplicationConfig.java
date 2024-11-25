package com.team1.cineBox.config;

import com.team1.cineBox.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	private final UserRepository userRepository;

	/**
	 * Crea un bean di UserDetailsService ritornando l'utente in base alla mail
	 *
	 * @return i dettagli dell'utente trovato
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("user with username " + username + " not found"));
	}

	/**
	 * Crea un Bean di AutenticationProvider ritornando un Dao con impostato lo usertDetailService e il passwordEncoder
	 *
	 * @return Un authenticationProvider
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	/**
	 * Crea un bean per ottenere l'istanza di AuthenticationManager utilizzando la configurazione Spring Security.
	 *
	 * @param config la configurazione per l'autenticazione
	 * @return l'authenticationManager del config passato in ingresso
	 * @throws Exception
	 */
	@Bean
	public AuthenticationManager authenticationManager( AuthenticationConfiguration config ) throws Exception {
		return config.getAuthenticationManager();
	}

	/**
	 * Crea un bean per il passwordEncoder
	 *
	 * @return Una nuova istanza di BCryptPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
