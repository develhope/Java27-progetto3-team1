package com.team1.dealerApp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthFilter;

	private final AuthenticationProvider authenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable) // Disabilitiamo CSRF poiché gestiamo sessioni stateless
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/user/register", "/auth/login", "/auth/admin/register").permitAll() // Rotte di autenticazione pubbliche
						.requestMatchers("/a/**").hasRole("ADMIN")
						.requestMatchers("/u/**").hasAnyRole("USER", "ADMIN")
						.anyRequest().authenticated() // Tutte le altre richiedono autenticazione
				)
				.sessionManagement(session -> session.sessionCreationPolicy(STATELESS) // Disabilitare sessioni per gestire il login in maniera stateless tramite token
				)
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Aggiunge il filtro JWT
		return http.build();
	}
}
