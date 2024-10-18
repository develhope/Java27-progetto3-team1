package com.team1.dealerApp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // Disabilita CSRF per semplicità
				.authorizeHttpRequests(auth -> auth.requestMatchers("/login", "user/register", "/public/**").permitAll() // Login e registrazione aperti a tutti
						.anyRequest().authenticated() // Tutte le altre richieste richiedono autenticazione
				).formLogin(form -> form.loginPage("/login") // URL della pagina di login
						.permitAll() // Permette l'accesso alla pagina di login
				).logout(logout -> logout.permitAll() // Logout accessibile a tutti
				);
		return http.build();
	}
}
