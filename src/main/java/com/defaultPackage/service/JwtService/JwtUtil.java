package com.defaultPackage.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import com.defaultPackage.model.User;
import com.defaultPackage.request.UserCreationRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	
	private String jwtSecretKey = "My-Secret-Key:@%@^)@&@%!%!rhrh9pqr9q9ur9qwdacndhcpad9q9d9n11020812038";
	private static final long EXPIRATION_TIME = 1000*60*60; //1hour
	 
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
	}

	public String getUsernameFromToken(String token) {
		 Claims claims =  Jwts.parser()
	                .verifyWith(getSecretKey())
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();
	        return claims.getSubject();
	}

	public String getToken(UserCreationRequest user) {
		  return Jwts.builder()
	                .subject(user.getUserEmail())
	                .issuedAt(new Date())
	                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith(getSecretKey())
	                .compact();
	}

}
