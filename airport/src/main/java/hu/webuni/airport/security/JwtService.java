package hu.webuni.airport.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JwtService {

	private static final String AUTH = "auth";

	//ez a három mehet konfig-ba is
	long TOKEN_LIMIT = TimeUnit.MINUTES.toMillis(5);
	String issuer = "AirportApp";
	Algorithm alg = Algorithm.HMAC256("mysecret");

	public String createJwtToken(UserDetails principal) {
		return JWT.create()
					.withSubject(principal.getUsername())
					.withArrayClaim("auth", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
					.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_LIMIT))
					.withIssuer(issuer)
					.sign(alg);
	}

	public UserDetails parseJwt(String jwtToken) {
		DecodedJWT decodedJwt = JWT.require(alg)
			.withIssuer(issuer)
			.build()
			.verify(jwtToken);
						//user // jelszó bármi lehet itt
		return new User(decodedJwt.getSubject(),
						"dummy",
						decodedJwt.getClaim(AUTH)
									.asList(String.class)
									.stream()
									.map(SimpleGrantedAuthority::new)
									.collect(Collectors.toList()));
	}

}
