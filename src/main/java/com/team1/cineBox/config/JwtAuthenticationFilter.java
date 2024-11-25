package com.team1.cineBox.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	/**
	 * Filtra le richieste HTTP, verificando la presenza e la validità del token JWT nell'header di autorizzazione.
	 * Se il token è valido, autentica l'utente e imposta il contesto di sicurezza.
	 * @param request richiesta HTTP in ingresso
	 * @param response richiesta http in uscita
	 * @param filterChain la catena di filtri successivi
	 * @throws IOException Se si verifica un errore di I/O durante il processo di richiesta
	 * @throws ServletException se si verifica un problema durante il processo di filtraggio
	 */
	@Override
	protected void doFilterInternal( @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain ) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String jwtToken;
		String userEmail;

		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwtToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(jwtToken);
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
			if( jwtService.isTokenValid(jwtToken, userDetails) ) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);
			}

		}
		filterChain.doFilter(request, response);
	}
}
