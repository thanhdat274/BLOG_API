package vn.com.blogapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class CheckToken {
	@Value("${app.jwt.secret}")
	private String SECRET_KEY;

	public String checkToken(String token) {
		log.info("isValidToken: " + token);
		try {
			log.info("Secret key: " + SECRET_KEY);
			// Decode the token and retrieve claims (payload) from the token.
			Claims claims = Jwts.parser()
					.setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
					.parseClaimsJws(token)
					.getBody();
			log.info("Claims: " + claims);
			return claims.getSubject();
		} catch (ExpiredJwtException ex) {
			log.error("Token has expired. Please log in again! ", ex);
			// Handle the exception if the token has expired.
			return null;
		} catch (Exception ex) {
			log.error("Error while authenticating the token! ", ex);
			// Handle other exceptions if there is an issue with token authentication.
			return null;
		}
	}
}
