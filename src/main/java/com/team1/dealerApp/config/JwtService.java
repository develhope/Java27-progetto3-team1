package com.team1.dealerApp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

	/**
	 * La chiave segreta a 256bit utilizzata per firmare il token
	 */
	@Value("${application.security.jwt.secret-key}")
	private String secretKey;
	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;

	/**
	 * Recupera lo username dell'utente dal token
	 *
	 * @param jwtToken Il token in formato stringa
	 * @return Lo userName
	 */
	public String extractUsername( String jwtToken ) {
		return extractClaims(jwtToken, Claims::getSubject);
	}

	/**
	 * Estrae un Claim dal token passato in ingresso e li risolve tramite il metodo passato in ingresso
	 *
	 * @param jwtToken Il token da cui estrarre i claim.
	 * @param claimsResolver Il metodo da applicare ai claim estratti per risolvere il tipo desiderato.
	 * @param <T> Il tipo del claim da risolvere.
	 * @return Il claim risolto di tipo T.
	 *
	 * @throws IllegalArgumentException se il JWT Token è non valido o nullo.
	 */
	public <T> T extractClaims( String jwtToken, Function <Claims, T> claimsResolver ) {
		final Claims claims = extractAllClaims(jwtToken);
		return claimsResolver.apply(claims);
	}

	/**
	 * Genera un token senza extraClaims, inviando una mappa vuota in input
	 *
	 * @param userDetails La rappresentazione dell'utente
	 * @return Il token generato
	 */
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap <>(), userDetails);
	}

	/**
	 * Genera un nuovo JwtToken
	 *
	 * @param extraClaims La mappa che contiene i claim aggiuntivi da inserire nel token
	 * @param userDetails La rappresentazione dell'utente
	 * @return Il token generato
	 */
	public String generateToken ( Map <String, Object> extraClaims, UserDetails userDetails ) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	/**
	 * Controlla che il token sia valido
	 *
	 * @param token Il token in formato stringa
	 * @param userDetails La rappresentazione dell'utente
	 * @return Un booleano che indica la validità del Token
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	/**
	 * Controlla che il token non sia scaduto
	 *
	 * @param jwtToken Il token in formato stringa
	 * @return Un booleano che indica la validità del Token
	 */
	private boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
	}

	/**
	 * Estrae il claim contente la data di scadenza del token
	 *
	 * @param jwtToken Il token in formato stringa
	 * @return La data di scadenza del token
	 */
	private Date extractExpiration( String jwtToken ) {
		return extractClaims(jwtToken, Claims::getExpiration);
	}

	/**
	 * Estrae tutti i claim dal token
	 *
	 * @param jwtToken Il token in formato stringa
	 * @return Un set di claim
	 */
	private Claims extractAllClaims( String jwtToken ) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(jwtToken)
				.getBody();
	}

	/**
	 * Recupera un'istanza della chiave usata per firmare il token
	 *
	 * @return Un'istanza della chiave
	 */
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
